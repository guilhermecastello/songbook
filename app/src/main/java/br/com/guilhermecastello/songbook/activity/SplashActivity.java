package br.com.guilhermecastello.songbook.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.file.SongFile;
import br.com.guilhermecastello.songbook.rnbd.SongRN;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.util.QRCodeUtil;
import br.com.guilhermecastello.songbook.util.Util;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    protected final int PERMISSIONS_REQUEST = 1;

    private ProgressBar mPgrInicializa;
    private TextView mTxvStatus;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                                                       | View.SYSTEM_UI_FLAG_FULLSCREEN
                                                       | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                       | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                                       | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                       | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };


    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);

        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);
        mPgrInicializa = (ProgressBar) findViewById(R.id.pgrInicializa);
        mTxvStatus = (TextView) findViewById(R.id.txvStatus);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mVisible = false;
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);

        AsyncCaller task = new AsyncCaller();
        task.setProgressBar(mPgrInicializa);
        task.setTextView(mTxvStatus);
        task.execute();
    }


    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void verifyPermissions() {
        String[] permissions = Util.getAppPremissions(this);

        if (permissions != null) {
            for (String permission : permissions) {
                // Verificar se existe alguma permissao faltando para funcionamento do app
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    Util.requestPermission(this, permissions, PERMISSIONS_REQUEST);
                    return;
                }
            }
        }
    }


    private class AsyncCaller extends AsyncTask<Void, Integer, Void> {
        ProgressBar bar;
        TextView status;

        private SongFile mSongFile = null;
        private List<SongType> mSongs;
        private SongRN mSongRN;

        public void setProgressBar(ProgressBar bar) {
            this.bar = bar;
        }

        public void setTextView(TextView status) {
            this.status = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.status.setText("Verificando permissões");
            verifyPermissions();


            mSongRN = new SongRN(getBaseContext());
            mSongFile = new SongFile(getBaseContext());

            mSongs = mSongFile.readAllSongsToImport(true);

            this.bar.setProgress(0);
            this.bar.setMax(mSongs.size());
            this.status.setText("Importando músicas");
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < mSongs.size(); i++) {
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                }

                SongType song = mSongRN.getByNumber(mSongs.get(i));
                if (song == null) {
                    mSongRN.createNewSong(mSongs.get(i));
                }
                publishProgress(i);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            startActivity(new Intent(getBaseContext(), SongListActivity.class));
            finish();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (this.bar != null) {
                bar.setProgress(values[0]);
                this.status.setText(mSongs.get(values[0]).getName());
            }
        }
    }

}
