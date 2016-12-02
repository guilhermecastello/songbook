package br.com.guilhermecastello.songbook.rnbd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.guilhermecastello.songbook.type.PhraseType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.util.Util;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class PhraseRN extends AppRN<PhraseType> {

    private String   mTableName = "Phrase";

    private String[] mColumns   = new String[] { "id", "idVerse", "phrase","indSing" };

    public PhraseRN(Context context) {
        super(context);
    }



    public Long insert(PhraseType type, SQLiteDatabase db) {
        // Faz a inclusao
        ContentValues ctv = new ContentValues();
        ctv.put("idVerse", type.getIdVerse());
        ctv.put("phrase", type.getPhrase());
        ctv.put("indSing", type.getIndSing());


        long id = db.insertOrThrow(mTableName, null, ctv);

        return id;
    }

    @Override
    public void insert(PhraseType type) {
        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();

            // Inicia transacao
            db.beginTransaction();

            // Faz a inclusao
            ContentValues ctv = new ContentValues();
            ctv.put("idVerse", type.getIdVerse());
            ctv.put("phrase", type.getPhrase());
            ctv.put("indSing", type.getIndSing());

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

    @Override
    public PhraseType update(PhraseType type) {
        return null;
    }

    @Override
    public boolean delete(PhraseType type) {
        return false;
    }

    @Override
    public PhraseType get(Long id) {
        return null;
    }

    @Override
    public List<PhraseType> list(PhraseType type) {
        List<PhraseType> lista = new ArrayList<PhraseType>();
        PhraseType phraseTypeVolta = null;

        SQLiteDatabase db = null;
        Cursor rs = null;

        try {
            db = getReadableDatabase();

            String[] param = new String[] { String.valueOf(type.getIdVerse()) };

            rs = db.query(mTableName, mColumns, "idVerse=?", param, null, null, null);

            while (rs.moveToNext()) {
                phraseTypeVolta = new PhraseType();
                phraseTypeVolta.setId(Util.getLongValue(rs, "id"));
                phraseTypeVolta.setIdVerse(Util.getLongValue(rs, "idVerse"));
                phraseTypeVolta.setPhrase(Util.getStringValue(rs, "phrase"));
                phraseTypeVolta.setIndSing(Util.getByteValue(rs, "indSing"));

                lista.add(phraseTypeVolta);
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
