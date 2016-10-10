package br.com.guilhermecastello.songbook;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.guilhermecastello.songbook.util.SongbookDB;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class SongbookApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //net.sqlcipher.database.SQLiteDatabase.loadLibs(getApplicationContext());
        Log.w("SongbookApplication", "onCreate");
        mContext = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.w("SongbookApplication", "onTerminate");
    }

    public static Context getContext() {
        return mContext;
    }

    public SQLiteOpenHelper getOpenHelper(Context context) {
        return new SongbookDB(context);
    }


    public SQLiteDatabase getWritableDatabase() {
        return getOpenHelper(getApplicationContext()).getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return getOpenHelper(getApplicationContext()).getReadableDatabase();
    }
}
