package br.com.guilhermecastello.songbook.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.adapter.SongImportAdapter;
import br.com.guilhermecastello.songbook.file.SongFile;
import br.com.guilhermecastello.songbook.rnbd.PlaylistRN;
import br.com.guilhermecastello.songbook.rnbd.SongRN;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.util.QRCodeUtil;
import br.com.guilhermecastello.songbook.util.Util;

public class PlaylistImportActivity extends BaseActivity {

    private final int READ_QR_CODE_RC = 1;

    private SongRN mSongRN;

    private PlaylistRN mPlaylistRN;

    private PlaylistType mPlaylist = null;

    private ProgressBar mPrbImport;

    private Button mBtnImport;

    private ListView mLstView;

    private SongImportAdapter mAdapter;

    @Override
    public boolean showNavigationMenu() {
        return false;
    }

    @Override
    public boolean showBackButtom() {
        return true;
    }

    @Override
    public int getNavId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_import_activity);

        mSongRN = new SongRN(getBaseContext());

        mPrbImport = (ProgressBar) findViewById(R.id.prbImport);
        mBtnImport = (Button) findViewById(R.id.btnImport);
        mLstView = (ListView) findViewById(android.R.id.list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playlist_import_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnuImportPlaylistFromQRCode) {
            startActivityForResult(new Intent(getBaseContext(), QRCodeReaderActivity.class), READ_QR_CODE_RC);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showSongsToImport() {
        mAdapter = new SongImportAdapter(getBaseContext(), mPlaylist.getSongs());
        mLstView.setAdapter(mAdapter);

        if (mPlaylist.getSongs() != null && mPlaylist.getSongs().size() > 0) {
            mBtnImport.setEnabled(true);
        }
    }


    public void btnImportOnClick(View view) {

        mPlaylistRN = new PlaylistRN(getBaseContext());

//        try {
            PlaylistType playlistImported = mPlaylistRN.importPlaylist(mPlaylist);

            if (playlistImported != null) {
                mPlaylist = playlistImported;
            }
//        } catch (Exception ex) {
//            Util.showWarning(getBaseContext(), ex.getMessage());
//        }

        showSongsToImport();

        setResult(RESULT_OK);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String qrCode = data.getExtras().getString("qrCode");

            PlaylistType playlist = QRCodeUtil.readPlaylistFromQRCode(qrCode);

            mPlaylist = playlist;

            showSongsToImport();
        }
    }
}
