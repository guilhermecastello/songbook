package br.com.guilhermecastello.songbook.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.file.SongFile;
import br.com.guilhermecastello.songbook.type.SongType;

public class SongImportActivity extends BaseActivity {


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
                Toast.makeText(this, songType.getName(), Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnImportFromQRCodeOnClick(View view) {
        Toast.makeText(this, "Em desenvolvimento", Toast.LENGTH_SHORT).show();
    }
}
