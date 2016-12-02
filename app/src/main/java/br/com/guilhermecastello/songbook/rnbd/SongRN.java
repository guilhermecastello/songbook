package br.com.guilhermecastello.songbook.rnbd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.guilhermecastello.songbook.enumeration.LanguageEnum;
import br.com.guilhermecastello.songbook.type.PhraseType;
import br.com.guilhermecastello.songbook.type.PlaylistSongType;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.type.VerseType;
import br.com.guilhermecastello.songbook.util.Util;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class SongRN extends AppRN<SongType> {

    private String mTableName = "Song";

    private String[] mColumns = new String[]{"id", "name", "number", "language"};

    public SongRN(Context context) {
        super(context);
    }

    public void createNewSong(SongType songType) {
        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();
            // Inicia transacao
            db.beginTransaction();

            VerseRN verseRN = new VerseRN(getContext());
            PhraseRN phraseRN = new PhraseRN(getContext());

            long idSong = this.insert(songType, db);

            if (idSong > 0) {
                for (VerseType verse : songType.getVerses()) {
                    verse.setIdSong(idSong);
                    long idVerse = verseRN.insert(verse, db);

                    if (idVerse > 0) {
                        for (PhraseType phrase : verse.getPhrases()) {
                            phrase.setIdVerse(idVerse);
                            phraseRN.insert(phrase, db);
                        }
                    }
                }
            }
            // Finaliza transacao
            db.setTransactionSuccessful();
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
    }

    public SongType openSong(Long id) {

        VerseRN verseRN = new VerseRN(getContext());
        PhraseRN phraseRN = new PhraseRN(getContext());

        SongType songTypeVolta = this.get(id);

        VerseType verse = new VerseType();
        verse.setIdSong(id);

        List<VerseType> verses = verseRN.list(verse);

        PhraseType phraseType = null;
        for (VerseType verseType : verses) {
            phraseType = new PhraseType();
            phraseType.setIdVerse(verseType.getId());

            List<PhraseType> phrases = phraseRN.list(phraseType);
            if (phrases != null) {
                verseType.setPhrases(phrases);
            }
        }

        songTypeVolta.setVerses(verses);

        return songTypeVolta;
    }

    public SongType getNextSong(SongType songType) {
        List<SongType> lista = new ArrayList<SongType>();
        SongType songTypeVolta = null;
        StringBuilder sql = new StringBuilder();
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            sql.append("   SELECT S.id ");
            sql.append("         ,S.name ");
            sql.append("         ,S.number ");
            sql.append("         ,S.language ");
            sql.append("     FROM Song  S ");
            sql.append("    WHERE S.number > ? ");
            sql.append(" ORDER BY S.number ");
            sql.append("    LIMIT 1 ");

            rs = db.rawQuery(sql.toString(), new String[]{songType.getNumber().toString()});

            if (rs.moveToNext()) {
                songTypeVolta = new SongType();
                songTypeVolta.setId(Util.getLongValue(rs, "id"));
                songTypeVolta.setName(Util.getStringValue(rs, "name"));
                songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
                songTypeVolta.setLanguage(Util.getShortValue(rs, "language"));
            }

        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return songTypeVolta;
    }

    public SongType getNextSongFromPlaylist(SongType songType, PlaylistType playlistType) {
        List<SongType> lista = new ArrayList<SongType>();
        SongType songTypeVolta = null;
        StringBuilder sql = new StringBuilder();
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            PlaylistRN playlistRN = new PlaylistRN(getContext());

            PlaylistSongType psType = playlistRN.getPlaylistSong(songType, playlistType, db);

            if (psType != null) {
                sql.append("   SELECT S.id ");
                sql.append("         ,S.name ");
                sql.append("         ,S.number ");
                sql.append("         ,S.language ");
                sql.append("     FROM Song  S ");
                sql.append("         ,PlaylistSongs PS ");
                sql.append("    WHERE S.id = PS.idSong ");
                sql.append("      AND PS.songOrder = ? ");
                sql.append("      AND PS.idPlaylist = ? ");

                int nextOrder = psType.getSongOrder() + 1;

                rs = db.rawQuery(sql.toString(), new String[]{String.valueOf(nextOrder), playlistType.getId().toString()});

                if (rs.moveToNext()) {
                    songTypeVolta = new SongType();
                    songTypeVolta.setId(Util.getLongValue(rs, "id"));
                    songTypeVolta.setName(Util.getStringValue(rs, "name"));
                    songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
                    songTypeVolta.setLanguage(Util.getShortValue(rs, "language"));
                }
            }
        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return songTypeVolta;
    }

    public SongType getPreviousSong(SongType songType) {
        SongType songTypeVolta = null;
        StringBuilder sql = new StringBuilder();
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            sql.append("   SELECT S.id ");
            sql.append("         ,S.name ");
            sql.append("         ,S.number ");
            sql.append("         ,S.language ");
            sql.append("     FROM Song  S ");
            sql.append("    WHERE S.number < ? ");
            sql.append(" ORDER BY S.number DESC");
            sql.append("    LIMIT 1 ");

            rs = db.rawQuery(sql.toString(), new String[]{songType.getNumber().toString()});

            if (rs.moveToNext()) {
                songTypeVolta = new SongType();
                songTypeVolta.setId(Util.getLongValue(rs, "id"));
                songTypeVolta.setName(Util.getStringValue(rs, "name"));
                songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
                songTypeVolta.setLanguage(Util.getShortValue(rs, "language"));
            }

        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return songTypeVolta;
    }

    public SongType getPreviousSongFromPlaylist(SongType songType, PlaylistType playlistType) {
        List<SongType> lista = new ArrayList<SongType>();
        SongType songTypeVolta = null;
        StringBuilder sql = new StringBuilder();
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            PlaylistRN playlistRN = new PlaylistRN(getContext());
            PlaylistSongType psType = playlistRN.getPlaylistSong(songType, playlistType, db);

            if (psType != null) {
                sql.append("   SELECT S.id ");
                sql.append("         ,S.name ");
                sql.append("         ,S.number ");
                sql.append("         ,S.language ");
                sql.append("     FROM Song  S ");
                sql.append("         ,PlaylistSongs PS ");
                sql.append("    WHERE S.id = PS.idSong ");
                sql.append("      AND PS.songOrder = ? ");
                sql.append("      AND PS.idPlaylist = ? ");

                int nextOrder = psType.getSongOrder() - 1;

                rs = db.rawQuery(sql.toString(), new String[]{String.valueOf(nextOrder), playlistType.getId().toString()});

                if (rs.moveToNext()) {
                    songTypeVolta = new SongType();
                    songTypeVolta.setId(Util.getLongValue(rs, "id"));
                    songTypeVolta.setName(Util.getStringValue(rs, "name"));
                    songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
                    songTypeVolta.setLanguage(Util.getShortValue(rs, "language"));
                }
            }
        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return songTypeVolta;
    }

    public Long insert(SongType song, SQLiteDatabase db) {
        ContentValues ctv = new ContentValues();
        ctv.put("name", song.getName());
        ctv.put("number", song.getNumber());
        ctv.put("language", song.getLanguage());

        long id = db.insertOrThrow(mTableName, null, ctv);

        return id;
    }


    @Override
    public void insert(SongType song) {

        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();

            // Inicia transacao
            db.beginTransaction();

            long id = this.insert(song, db);

            // Finaliza transacao
            db.setTransactionSuccessful();

        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
    }

    @Override
    public SongType update(SongType type) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();

            ContentValues ctv = new ContentValues();
            ctv.put("name", type.getName());
            ctv.put("number", type.getNumber());
            ctv.put("language", type.getLanguage());

            String[] param = new String[]{String.valueOf(type.getId())};

            db.update(mTableName, ctv, "id=?", param);

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return null;
    }


    @Override
    public boolean delete(SongType type) {
        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();
            String[] args = new String[]{String.valueOf(type.getId())};

            // Exclui a Abordagem
            db.delete(mTableName, "id=?", args);

        } finally {
            if (db != null)
                db.close();
        }

        return true;
    }

    @Override
    public SongType get(Long id) {
        SongType songTypeVolta = null;
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            String[] param = new String[]{String.valueOf(id)};

            rs = db.query(mTableName, mColumns, "id=?", param, null, null, null);

            if (rs.moveToFirst()) {
                songTypeVolta = new SongType();
                songTypeVolta.setId(Util.getLongValue(rs, "id"));
                songTypeVolta.setName(Util.getStringValue(rs, "name"));
                songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
                songTypeVolta.setLanguage(Util.getShortValue(rs, "language"));
            }

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return songTypeVolta;
    }

    public SongType getByNumber(SongType songType) {
        SongType songTypeVolta = null;
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            String[] param = new String[]{String.valueOf(songType.getNumber()), String.valueOf(songType.getLanguage())};

            rs = db.query(mTableName, mColumns, "number=? AND language=?", param, null, null, null);

            if (rs.moveToFirst()) {
                songTypeVolta = new SongType();
                songTypeVolta.setId(Util.getLongValue(rs, "id"));
                songTypeVolta.setName(Util.getStringValue(rs, "name"));
                songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
                songTypeVolta.setLanguage(Util.getShortValue(rs, "language"));
            }
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return songTypeVolta;
    }

    public SongType getByNumber(SongType songType, SQLiteDatabase db) {
        SongType songTypeVolta = null;
        Cursor rs = null;

        String[] param = new String[]{String.valueOf(songType.getNumber()), String.valueOf(songType.getLanguage())};

        rs = db.query(mTableName, mColumns, "number=? AND language=?", param, null, null, null);

        if (rs.moveToFirst()) {
            songTypeVolta = new SongType();
            songTypeVolta.setId(Util.getLongValue(rs, "id"));
            songTypeVolta.setName(Util.getStringValue(rs, "name"));
            songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
            songTypeVolta.setLanguage(Util.getShortValue(rs, "language"));
        }

        return songTypeVolta;
    }

    public List<SongType> list() {
        return list(new SongType());
    }

    @Override
    public List<SongType> list(SongType songType) {
        List<SongType> lista = new ArrayList<SongType>();
        SongType songTypeVolta = null;
        StringBuilder sql = new StringBuilder();
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            sql.append("  SELECT S.id ");
            sql.append("        ,S.name ");
            sql.append("        ,S.number ");
            sql.append("        ,S.language ");
            sql.append("    FROM Song  S ");
            sql.append("   WHERE S.language = ?");
            sql.append(" ORDER BY S.number ");

            String[] args = new String[1];
            if (songType.getLanguage() != null) {
                args[0] = songType.getLanguage().toString();
            } else {
                args[0] = LanguageEnum.getByLanguage(Locale.getDefault().getLanguage()).getCodigo().toString();
            }

            rs = db.rawQuery(sql.toString(), args);

            while (rs.moveToNext()) {
                songTypeVolta = new SongType();
                songTypeVolta.setId(Util.getLongValue(rs, "id"));
                songTypeVolta.setName(Util.getStringValue(rs, "name"));
                songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
                songTypeVolta.setLanguage(Util.getShortValue(rs, "language"));

                lista.add(songTypeVolta);
            }

        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return lista;
    }

    public List<SongType> listPlaylist(PlaylistType playlist) {
        List<SongType> lista = new ArrayList<SongType>();
        SongType songTypeVolta = null;
        StringBuilder sql = new StringBuilder();
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            sql.append("  SELECT S.id ");
            sql.append("        ,S.name ");
            sql.append("        ,S.number ");
            sql.append("        ,S.language ");
            sql.append("    FROM Song  S ");
            sql.append("        ,PlaylistSongs  PS ");
            sql.append("   WHERE S.id = PS.idSong ");
            sql.append("     AND PS.idPlaylist = ? ");
            sql.append(" ORDER BY PS.songOrder ");

            rs = db.rawQuery(sql.toString(), new String[]{String.valueOf(playlist.getId().toString())});

            while (rs.moveToNext()) {
                songTypeVolta = new SongType();
                songTypeVolta.setId(Util.getLongValue(rs, "id"));
                songTypeVolta.setName(Util.getStringValue(rs, "name"));
                songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
                songTypeVolta.setLanguage(Util.getShortValue(rs, "language"));

                lista.add(songTypeVolta);
            }

        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return lista;
    }


}


