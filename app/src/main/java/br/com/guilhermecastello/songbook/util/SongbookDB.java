package br.com.guilhermecastello.songbook.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class SongbookDB extends SQLiteOpenHelper {

    private Context context = null;

    private static int versao  = 2;

    public SongbookDB(Context context) {
        super(context, getDbName(), null, versao);
        this.context = context;
    }

    private static String getDbName() {
        String dbName = "SongbookDB.db";
        return dbName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createTableSong(db);
        this.createTableVerse(db);
        this.createTablePhrase(db);

        // Faz demais atualizacoes
        onUpgrade(db, 0, 0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            this.createTablePlaylist(db);
            this.createTablePlaylistSongs(db);
        }
        if (oldVersion < 3) {

        }
    }

    private void createTableSong(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE [Song] ( ");
        sql.append("       [id]       INTEGER PRIMARY KEY AUTOINCREMENT ");
        sql.append("      ,[name]     TEXT ");
        sql.append("      ,[number]   INTEGER ");
        sql.append("      ,[language] NUMBER ");
        sql.append(" );");
        db.execSQL(sql.toString());

        //db.execSQL("INSERT INTO [TabelaLocal] (codTabela, descricao, qtdeReg, dthAtualizacao) VALUES ('VEIC_IRREG', 'Veículos Irregulares', 0, null);");
    }

    private void createTableVerse(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE [Verse] ( ");
        sql.append("       [id]         INTEGER PRIMARY KEY AUTOINCREMENT ");
        sql.append("      ,[idSong]     INTEGER ");
        sql.append("      ,[chorus]     INTEGER ");
        sql.append("      ,[typeVoice]  INTEGER ");
        sql.append(" );");
        db.execSQL(sql.toString());

        //db.execSQL("INSERT INTO [TabelaLocal] (codTabela, descricao, qtdeReg, dthAtualizacao) VALUES ('VEIC_IRREG', 'Veículos Irregulares', 0, null);");
    }

    private void createTablePhrase(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE [Phrase] ( ");
        sql.append("       [id]     INTEGER PRIMARY KEY AUTOINCREMENT ");
        sql.append("      ,[idVerse] INTEGER ");
        sql.append("      ,[phrase]  TEXT ");
        sql.append("      ,[indSing] INTEGER ");
        sql.append(" );");
        db.execSQL(sql.toString());

        //db.execSQL("INSERT INTO [TabelaLocal] (codTabela, descricao, qtdeReg, dthAtualizacao) VALUES ('VEIC_IRREG', 'Veículos Irregulares', 0, null);");
    }

    private void createTablePlaylist(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE [Playlist] ( ");
        sql.append("       [id]   INTEGER PRIMARY KEY AUTOINCREMENT ");
        sql.append("      ,[name] INTEGER ");
        sql.append(" );");
        db.execSQL(sql.toString());
    }

    private void createTablePlaylistSongs(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE [PlaylistSongs] ( ");
        sql.append("       [idPlaylist] INTEGER ");
        sql.append("      ,[idSong]     INTEGER ");
        sql.append("      ,[songOrder]  INTEGER ");
        sql.append(" );");
        db.execSQL(sql.toString());
    }

}


