package br.com.guilhermecastello.songbook.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.enumeration.IndSingEnum;
import br.com.guilhermecastello.songbook.enumeration.IndYesNoEnum;
import br.com.guilhermecastello.songbook.file.SongFile;
import br.com.guilhermecastello.songbook.rnbd.SongRN;
import br.com.guilhermecastello.songbook.type.PhraseType;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.type.VerseType;

public class SongViewActivity extends BaseActivity {

    private final float MIN_TEXT_SIZE = 40;
    private final float MAX_TEXT_SIZE = 110;

    private GestureDetector gestureDetector;

    private ScaleGestureDetector gestureDetectorZoom;

    private SongRN songRN;

    private SongType songTypeOpened;

    private PlaylistType playlistOpened;

    private TextView mTxvTitle;

    private LinearLayout mPnlSong;


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
        setContentView(R.layout.song_view_activity);

        View view = findViewById(android.R.id.content);
        Animation mLoadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        mLoadAnimation.setDuration(1000);
        view.startAnimation(mLoadAnimation);

        mTxvTitle = (TextView) findViewById(R.id.txvTitle);
        mPnlSong = (LinearLayout) findViewById(R.id.pnlSong);

        songRN = new SongRN(getBaseContext());

        gestureDetector = new GestureDetector(this, new MyFlingGestureDetector());
        gestureDetectorZoom = new ScaleGestureDetector(this, new ZoomGestureDetector());

        Bundle param = getIntent().getExtras();

        if (param != null) {
            Long idSong = param.getLong("idSong", 0);
            if (idSong != 0) {
                setupSong(idSong);
            }

            Long idPlaylist = param.getLong("idPlaylist", 0);
            if (idPlaylist != 0) {
                playlistOpened = new PlaylistType();
                playlistOpened.setId(idPlaylist);
            }
        }

    }

    private void setupSong(Long idSong) {
        songTypeOpened = songRN.openSong(idSong);
        if (songTypeOpened != null) {

            mPnlSong.removeAllViews();

            mTxvTitle.setText(songTypeOpened.getNumber() + " - " + songTypeOpened.getName());

            StringBuilder verse = null;
            TextView txvVerse = null;
            for (VerseType verseType : songTypeOpened.getVerses()) {
                verse = new StringBuilder();
                txvVerse = new TextView(this);
                txvVerse.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTxvTitle.getTextSize());

                boolean bis = false;
                if (verseType.getChorus() != null) {
                    if (verseType.getChorus().equals(IndYesNoEnum.YES.getCodigo())) {
                        txvVerse.setTypeface(null, Typeface.BOLD);
                        verse.append("\n");
                        verse.append(getResources().getString(R.string.chorus_descr));
                    }
                }

                if (verseType.getTypeVoice() != null) {
                    verse.append("\n");
                    verse.append("Sopranos:");
                }

                //Controle de repetição da estrofe, pode ser BIS ou um numero X de vezes
                if (verseType.getIndBis() != null) {
                    if (verseType.getIndBis().equals(IndYesNoEnum.YES.getCodigo())) {
                        verse.append("- ");
                        bis = true;
                    }
                } else {
                    if (verseType.getxRepeat() != null) {
                        if (verseType.getxRepeat() > 0) {
                            verse.append(" (").append(verseType.getxRepeat()).append("X)");
                        }
                    }
                }

                verse.append(this.buildPhrases(verseType.getPhrases(), bis));

                if (verseType.getIndBis() != null) {
                    if (verseType.getIndBis().equals(IndYesNoEnum.YES.getCodigo())) {
                        verse.append(" (BIS)");
                    }
                }


                txvVerse.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                txvVerse.setText(verse.toString());


                mPnlSong.addView(txvVerse);
            }
        }
    }

    private String buildPhrases(List<PhraseType> phrases, boolean verseBis) {
        StringBuilder verse = new StringBuilder();
        for (PhraseType phraseType : phrases) {
            if (!verseBis) {
                verse.append("\n");
            }
            if (phraseType.getIndBis() != null) {
                if (phraseType.getIndBis().equals(IndYesNoEnum.YES.getCodigo())) {
                    verse.append("- ");
                }
            }

            verse.append(phraseType.getPhrase());
            if (phraseType.getIndSing() != null) {
                if (phraseType.getIndSing() > 0) {
                    verse.append(" (").append(IndSingEnum.get(phraseType.getIndSing()).getDescricao()).append(")");
                }
            }

            if (phraseType.getIndBis() != null) {
                if (phraseType.getIndBis().equals(IndYesNoEnum.YES.getCodigo())) {
                    verse.append(" (BIS)");
                }
            } else {
                if (phraseType.getxRepeat() != null) {
                    if (phraseType.getxRepeat() > 0) {
                        verse.append(" (").append(phraseType.getxRepeat()).append("X)");
                    }
                }
            }

            verseBis = false;
        }

        return verse.toString();
    }

    private void nextSong() {
        SongType nextSong = null;

        if (playlistOpened != null) {
            nextSong = this.songRN.getNextSongFromPlaylist(songTypeOpened, playlistOpened);
        } else {
            nextSong = this.songRN.getNextSong(songTypeOpened);
        }

        if (nextSong != null) {
            setupSong(nextSong.getId());
        }
    }

    private void previousSong() {
        SongType nextSong = null;

        if (playlistOpened != null) {
            nextSong = this.songRN.getPreviousSongFromPlaylist(songTypeOpened, playlistOpened);
        } else {
            nextSong = this.songRN.getPreviousSong(songTypeOpened);
        }

        if (nextSong != null) {
            setupSong(nextSong.getId());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean tratouEvento = false;

        if (event.getPointerCount() > 1) {
            tratouEvento = gestureDetectorZoom.onTouchEvent(event);
        } else {
            tratouEvento = gestureDetector.onTouchEvent(event);
        }


        if (tratouEvento) {
            return tratouEvento;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_view_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnuSongShowQRCode) {
            Intent it = new Intent(getBaseContext(), SongQRCodeActivity.class);
            it.putExtra("idSong", songTypeOpened.getId());
            startActivity(it);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyFlingGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private float swipeMinDistance = 100;
        private float swipeThreasholdVelocity = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {

                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > swipeMinDistance && Math.abs(velocityX) > swipeThreasholdVelocity) {
                        if (diffX > 0) {
                            previousSong();
                        } else {
                            nextSong();
                        }
                    }
                }

            } catch (Exception e) {
            }


            return false;
        }

    }

    private class ZoomGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float size = mTxvTitle.getTextSize();

            float factor = detector.getScaleFactor();

            float product = size * factor;

            if (product >= MIN_TEXT_SIZE && product <= MAX_TEXT_SIZE) {
                mTxvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);
                for (int i = 0; i < mPnlSong.getChildCount(); i++) {
                    TextView txv = (TextView) mPnlSong.getChildAt(i);
                    txv.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);
                }
            }


            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }


    }

}

