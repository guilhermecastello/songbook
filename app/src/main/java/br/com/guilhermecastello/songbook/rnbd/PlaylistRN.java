package br.com.guilhermecastello.songbook.rnbd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.guilhermecastello.songbook.enumeration.ImportStatusEnum;
import br.com.guilhermecastello.songbook.exception.RNException;
import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.SongbookApplication;
import br.com.guilhermecastello.songbook.type.PlaylistSongType;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.util.Util;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class PlaylistRN extends AppRN<PlaylistType> {

    private String mTableName = "Playlist";

    private String[] mColumns = new String[]{"id", "name",};

    public PlaylistRN(Context context) {
        super(context);
    }


    public PlaylistType importPlaylist(PlaylistType playlist) {
        SQLiteDatabase db = null;

        SongRN songRN = new SongRN(getContext());

        ArrayList<SongType> songs = new ArrayList<>();

        PlaylistSongType p = new PlaylistSongType();
        try {
            db = getWritableDatabase();

            if (playlist.getSongs() == null || playlist.getSongs().size() == 0) {
                throw new RNException(SongbookApplication.getContext().getString(R.string.playlist_no_songs));
            }

            // Inicia transacao
            db.beginTransaction();

            Long id = this.insert(playlist, db);

            playlist.setId(id);

            for (SongType songType : playlist.getSongs()) {
                SongType songFound = songRN.getByNumber(songType, db);
                if (songFound != null) {

                    ContentValues ctv = new ContentValues();
                    ctv.put("idSong", songFound.getId());
                    ctv.put("idPlaylist", playlist.getId());
                    ctv.put("songOrder", getNextOrderNumber(playlist, db));

                    db.insertOrThrow("PlaylistSongs", null, ctv);

                    songFound.setResult("Adicionada a playlist");
                    songFound.setStatus(ImportStatusEnum.IMPORTED.getCodigo());
                    songs.add(songFound);
                }
                else {
                    songType.setResult("Música não foi encontrada.");
                    songType.setStatus(ImportStatusEnum.FAIL.getCodigo());
                    songs.add(songType);
                }
            }

            playlist.setSongs(songs);

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

        return playlist;
    }


    public void addSongToPlaylist(SongType songType, PlaylistType playListType) {
        SQLiteDatabase db = null;
        PlaylistSongType p = new PlaylistSongType();
        try {
            db = getWritableDatabase();

            PlaylistSongType psType = this.getPlaylistSong(songType, playListType, db);
            if (psType != null) {
                throw new RNException(SongbookApplication.getContext().getString(R.string.playlist_duplicated_song));
            }

            // Inicia transacao
            db.beginTransaction();

            ContentValues ctv = new ContentValues();
            ctv.put("idSong", songType.getId());
            ctv.put("idPlaylist", playListType.getId());
            ctv.put("songOrder", getNextOrderNumber(playListType, db));

            long id = db.insertOrThrow("PlaylistSongs", null, ctv);

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

    public Boolean removeSongFromPlaylist(SongType songType, PlaylistType playListType) {
        Boolean deleted = false;
        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();

            // Inicia transacao
            db.beginTransaction();

            String[] args = new String[]{String.valueOf(songType.getId()), String.valueOf(playListType.getId())};

            // Exclui a Abordagem
            int id = db.delete("PlaylistSongs", "idSong=? AND idPlaylist = ?", args);

            deleted = id > 0;

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

        return deleted;
    }

    public boolean removePlaylist(PlaylistType playlistType) {
        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();

            db.beginTransaction();

            String[] args = new String[]{String.valueOf(playlistType.getId())};

            db.delete("PlaylistSongs", "idPlaylist = ?", args);

            this.delete(playlistType,db);

            db.setTransactionSuccessful();
        } finally {
            if (db != null) {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }

        return true;
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
            sql.append("     FROM Song  S ");
            sql.append("   WHERE S.number > ? ");
            sql.append(" ORDER BY S.number ");
            sql.append("    LIMIT 1 ");

            rs = db.rawQuery(sql.toString(), new String[]{songType.getNumber().toString()});

            if (rs.moveToNext()) {
                songTypeVolta = new SongType();
                songTypeVolta.setId(Util.getLongValue(rs, "id"));
                songTypeVolta.setName(Util.getStringValue(rs, "name"));
                songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
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
            sql.append("     FROM Song  S ");
            sql.append("   WHERE S.number < ? ");
            sql.append(" ORDER BY S.number DESC");
            sql.append("    LIMIT 1 ");

            rs = db.rawQuery(sql.toString(), new String[]{songType.getNumber().toString()});

            if (rs.moveToNext()) {
                songTypeVolta = new SongType();
                songTypeVolta.setId(Util.getLongValue(rs, "id"));
                songTypeVolta.setName(Util.getStringValue(rs, "name"));
                songTypeVolta.setNumber(Util.getIntegerValue(rs, "number"));
            }

        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return songTypeVolta;
    }

    public Long insert(PlaylistType type, SQLiteDatabase db) {
        ContentValues ctv = new ContentValues();
        ctv.put("name", type.getName());

        long id = db.insertOrThrow(mTableName, null, ctv);

        return id;
    }


    @Override
    public void insert(PlaylistType type) {

        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();

            // Inicia transacao
            db.beginTransaction();

            long id = this.insert(type, db);

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
    public PlaylistType update(PlaylistType type) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();

            ContentValues ctv = new ContentValues();
            ctv.put("name", type.getName());

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
    public boolean delete(PlaylistType type) {
        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();

            this.delete(type, db);

        } finally {
            if (db != null)
                db.close();
        }

        return true;
    }

    public boolean delete(PlaylistType type, SQLiteDatabase db) {

        String[] args = new String[]{String.valueOf(type.getId())};

        db.delete(mTableName, "id=?", args);

        return true;
    }


    @Override
    public PlaylistType get(Long id) {
        PlaylistType playlistTypeVolta = null;
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            String[] param = new String[]{String.valueOf(id)};

            rs = db.query(mTableName, mColumns, "id=?", param, null, null, null);

            if (rs.moveToFirst()) {
                playlistTypeVolta = new PlaylistType();
                playlistTypeVolta.setId(Util.getLongValue(rs, "id"));
                playlistTypeVolta.setName(Util.getStringValue(rs, "name"));
            }

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return playlistTypeVolta;
    }

    public List<PlaylistType> list() {
        return list(new PlaylistType());
    }

    @Override
    public List<PlaylistType> list(PlaylistType type) {
        List<PlaylistType> lista = new ArrayList<PlaylistType>();
        PlaylistType playlistTypeVolta = null;
        StringBuilder sql = new StringBuilder();
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            sql.append("  SELECT P.id ");
            sql.append("        ,P.name ");
            sql.append("    FROM Playlist  P ");

            rs = db.rawQuery(sql.toString(), null);

            while (rs.moveToNext()) {
                playlistTypeVolta = new PlaylistType();
                playlistTypeVolta.setId(Util.getLongValue(rs, "id"));
                playlistTypeVolta.setName(Util.getStringValue(rs, "name"));

                lista.add(playlistTypeVolta);
            }

        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return lista;
    }


    private Integer getNextOrderNumber(PlaylistType type, SQLiteDatabase db) {
        Integer nextOrder = 0;
        StringBuilder sql = new StringBuilder();
        Cursor rs = null;

        try {

            sql.append("  SELECT MAX(songOrder) as maxOrder");
            sql.append("    FROM PlaylistSongs ");
            sql.append("   WHERE idPlaylist = ? ");

            rs = db.rawQuery(sql.toString(), new String[]{type.getId().toString()});
            if (rs.moveToNext()) {
                Integer order = Util.getIntegerValue(rs, "maxOrder");
                if (order != null) {
                    nextOrder = order + 1;
                }
            }
        } finally {
            if (rs != null)
                rs.close();
        }
        return nextOrder;
    }

    public PlaylistSongType getPlaylistSong(SongType songType, PlaylistType playlistType, SQLiteDatabase db) {
        PlaylistSongType psTypeVolta = null;
        StringBuilder sql = new StringBuilder();

        Cursor rs = null;

        try {
            db = getReadableDatabase();

            sql.append("   SELECT PS.idSong ");
            sql.append("         ,PS.idPlaylist ");
            sql.append("         ,PS.songOrder ");
            sql.append("     FROM PlaylistSongs PS ");
            sql.append("    WHERE PS.idSong = ? ");
            sql.append("      AND PS.idPlaylist = ? ");

            rs = db.rawQuery(sql.toString(), new String[]{songType.getId().toString(), playlistType.getId().toString()});

            if (rs.moveToNext()) {
                psTypeVolta = new PlaylistSongType();
                psTypeVolta.setIdSong(Util.getLongValue(rs, "idSong"));
                psTypeVolta.setIdPlaylist(Util.getLongValue(rs, "idPlaylist"));
                psTypeVolta.setSongOrder(Util.getIntegerValue(rs, "songOrder"));
            }

        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return psTypeVolta;
    }


}
