package br.com.guilhermecastello.songbook.rnbd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.type.VerseType;
import br.com.guilhermecastello.songbook.util.Util;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class VerseRN extends AppRN<VerseType> {

    private String   mTableName = "Verse";


    private String[] mColumns   = new String[] { "id", "idSong", "chorus", "typeVoice" };
    public VerseRN(Context context) {
        super(context);
    }

    public void insert(VerseType verse) {

        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();

            // Inicia transacao
            db.beginTransaction();

            // Faz a inclusao
            ContentValues ctv = new ContentValues();
            ctv.put("idSong", verse.getIdSong());
            ctv.put("chorus", verse.getChorus());
            ctv.put("typeVoice", verse.getTypeVoice());

            long id = db.insertOrThrow(mTableName, null, ctv);

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

    public Long insert(VerseType verse, SQLiteDatabase db) {
        // Faz a inclusao
        ContentValues ctv = new ContentValues();
        ctv.put("idSong", verse.getIdSong());
        ctv.put("chorus", verse.getChorus());
        ctv.put("typeVoice", verse.getTypeVoice());

        long id = db.insertOrThrow(mTableName, null, ctv);

        return id;
    }

    @Override
    public VerseType update(VerseType type) {
        return null;
    }

    @Override
    public boolean delete(VerseType type) {
        return false;
    }

    @Override
    public VerseType get(Long id) {
        VerseType verseTypeVolta = null;
        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            String[] param = new String[] { String.valueOf(id) };

            rs = db.query(mTableName, mColumns, "id=?", param, null, null, null);

            if (rs.moveToFirst()) {
                verseTypeVolta = new VerseType();
                verseTypeVolta.setId(Util.getLongValue(rs, "id"));
                verseTypeVolta.setIdSong(Util.getLongValue(rs, "idSong"));
                verseTypeVolta.setChorus(Util.getByteValue(rs, "chorus"));
                verseTypeVolta.setTypeVoice(Util.getShortValue(rs, "typeVoice"));
            }

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        } finally {
            if (rs != null)
                rs.close();
            if (db != null)
                db.close();
        }
        return verseTypeVolta;
    }

    @Override
    public List<VerseType> list(VerseType type) {
        List<VerseType> lista = new ArrayList<VerseType>();
        VerseType verseTypeVolta = null;

        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            String[] param = new String[] { String.valueOf(type.getIdSong()) };

            rs = db.query(mTableName, mColumns, "idSong=?", param, null, null, null);

            while (rs.moveToNext()) {
                verseTypeVolta = new VerseType();
                verseTypeVolta.setId(Util.getLongValue(rs, "id"));
                verseTypeVolta.setIdSong(Util.getLongValue(rs, "idSong"));
                verseTypeVolta.setChorus(Util.getByteValue(rs, "chorus"));
                verseTypeVolta.setTypeVoice(Util.getShortValue(rs, "typeVoice"));

                lista.add(verseTypeVolta);
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
