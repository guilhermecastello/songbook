package br.com.guilhermecastello.songbook.rnbd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.com.guilhermecastello.songbook.SongbookApplication;
import br.com.guilhermecastello.songbook.util.ListaErros;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public abstract class AppRN<E> {

    private Context mContext       = null;

    private SongbookApplication mApplication = null;

    private ListaErros mListaErros    = null;

    public AppRN(Context context) {
        mContext = context;
        mApplication = (SongbookApplication) mContext.getApplicationContext();
    }

    public Context getContext() {
        return mContext;
    }

    public SongbookApplication getApp() {
        return mApplication;
    }

    public SQLiteDatabase getWritableDatabase() {
        return mApplication.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return mApplication.getReadableDatabase();
    }

    public ListaErros getListaErros() {
        if (mListaErros == null) {
            mListaErros = new ListaErros();
        }
        return mListaErros;
    }

    public void addErro(String descricao) {
        getListaErros().add(descricao);
    }

    public void limpaErros() {
        getListaErros().clear();
    }

    public abstract void insert(E type);
    public abstract E update(E type);
    public abstract boolean delete(E type);
    public abstract E get(Long id);
    public abstract List<E> list(E type);


}
