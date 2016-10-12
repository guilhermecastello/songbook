package br.com.guilhermecastello.songbook.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.file.SongFile;
import br.com.guilhermecastello.songbook.rnbd.SongRN;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.util.QRCodeUtil;

public class SongImportActivity extends BaseActivity {

    private SongRN mSongRN;
    private SongFile mSongFile = null;

    @Override
    public boolean showNavigationMenu() {
        return true;
    }

    @Override
    public boolean showBackButtom() {
        return false;
    }

    @Override
    public int getNavId() {
        return R.id.nav_import;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_import_activity);

        mSongRN = new SongRN(getBaseContext());
    }


    public void btnImportFromAssetsOnClick(View view) {
        try{
            mSongFile = new SongFile(getBaseContext());

            List<SongType> songs = mSongFile.readAllSongsToImport(true);

            for (SongType songType : songs) {
                Toast.makeText(this, songType.getName(), Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnImportFromHomeFolderOnClick(View view) {
        try{
            mSongFile = new SongFile(getBaseContext());

            List<SongType> songs = mSongFile.readAllSongsToImport(false);

            for (SongType songType : songs) {
                mSongRN.createNewSong(songType);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnImportFromQRCodeOnClick(View view) {
        startActivityForResult(new Intent(getBaseContext(), QRCodeReaderActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String qrCode = data.getExtras().getString("qrCode");

            SongType songType = QRCodeUtil.readSongFromQRCode(qrCode);

            Toast.makeText(this, songType.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}
