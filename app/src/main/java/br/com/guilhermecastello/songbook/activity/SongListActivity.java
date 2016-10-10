package br.com.guilhermecastello.songbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.SongbookApplication;
import br.com.guilhermecastello.songbook.adapter.SongAdapter;
import br.com.guilhermecastello.songbook.enumeration.IndSingEnum;
import br.com.guilhermecastello.songbook.enumeration.IndYesNoEnum;
import br.com.guilhermecastello.songbook.enumeration.LanguageEnum;
import br.com.guilhermecastello.songbook.rnbd.PlaylistRN;
import br.com.guilhermecastello.songbook.rnbd.SongRN;
import br.com.guilhermecastello.songbook.type.PhraseType;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.type.VerseType;
import br.com.guilhermecastello.songbook.util.Util;

public class SongListActivity extends BaseActivity {

    private final int ADD_SONG_TO_PLAYLIST_RC = 1;

    private ListView mLstView = null;
    private TextView mEmptyView = null;
    private EditText mTxtSearch = null;

    private SongAdapter mAdapter = null;

    private SongRN mSongRN = null;
    private PlaylistRN mPlaylistRN = null;
    private PlaylistType mPlaylist;

    private boolean hasNavigationMenu = true;

    @Override
    public boolean showNavigationMenu() {
        return hasNavigationMenu;
    }

    @Override
    public boolean showBackButtom() {
        return !hasNavigationMenu;
    }

    @Override
    public int getNavId() {
        return R.id.nav_songs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle param = getIntent().getExtras();
        if (param != null) {
            Long idPlaylist = param.getLong("idPlaylist", 0);
            if (idPlaylist != 0) {
                mPlaylist = new PlaylistType();
                mPlaylist.setId(idPlaylist);
                hasNavigationMenu = false;
            }
        }

        setContentView(R.layout.song_list_activity);


        mLstView = (ListView) findViewById(android.R.id.list);
        mEmptyView = (TextView) findViewById(android.R.id.empty);
        mTxtSearch = (EditText) findViewById(R.id.txtSearch);

        mLstView.setOnItemClickListener(new ListViewListenerImpl());
        registerForContextMenu(mLstView);
        mTxtSearch.addTextChangedListener(new TextWatcherImpl());


        mSongRN = new SongRN(getBaseContext());
        mPlaylistRN = new PlaylistRN(getBaseContext());

       //importSongs();

        refresh();
    }

    private void refresh() {
        List<SongType> lista = null;

        if (mPlaylist != null) {
            lista = mSongRN.listPlaylist(mPlaylist);
        } else {
            lista = mSongRN.list();
        }

        mAdapter = new SongAdapter(getBaseContext(), lista);
        mLstView.setAdapter(mAdapter);

        if (mAdapter.getCount() == 0) {
            mLstView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mLstView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }


    private void importSongs() {

        try {
            for (String song : getAssets().list("")) {
                readFile(song);
            }
        } catch (IOException e) {

        }
    }


    private void readFile(String nameArq) {
        SongType songType = new SongType();
        VerseType verseType = null;

        ArrayList<VerseType> verses = new ArrayList<VerseType>();
        ArrayList<PhraseType> phrases = new ArrayList<PhraseType>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(nameArq)));

            // do reading, usually loop until end of file reading
            String mLine;
            boolean firstRow = true;
            boolean repeatVerse = false;
            while ((mLine = reader.readLine()) != null) {
                //Toast.makeText(getBaseContext(), mLine, Toast.LENGTH_SHORT).show();
                if (firstRow) {
                    firstRow = false;
                    String[] fields = mLine.split("-");
                    if (fields.length == 2) {
                        songType.setNumber(Integer.valueOf(fields[0].trim()));
                        songType.setName(fields[1].trim());
                        LanguageEnum languageEnum = LanguageEnum.getByLanguage(Locale.getDefault().getLanguage());
                        if (languageEnum == null) {
                            languageEnum = LanguageEnum.PORTUGUESE;
                        }
                        songType.setLanguage(languageEnum.getCodigo());
                        //songType.setLanguage(LanguageEnum.PORTUGUESE.getCodigo());
                    } else if (fields.length == 3) {
                        songType.setNumber(Integer.valueOf(fields[0].trim()));
                        songType.setName(fields[1].trim());
                        LanguageEnum languageEnum = LanguageEnum.getByLanguage(fields[2].trim());
                        if (languageEnum == null) {
                            languageEnum = LanguageEnum.PORTUGUESE;
                        }
                        songType.setLanguage(languageEnum.getCodigo());

                    } else {
                        throw new RuntimeException("Arquivo fora do padrão");
                    }

                } else {
                    if (mLine.trim().equals("")) {
                        if (verseType != null) {
                            songType.addVerse(verseType);
                        }
                        // Toast.makeText(getBaseContext(), verseType.getPhrases().size() +"", Toast.LENGTH_SHORT).show();
                        verseType = new VerseType();
                    } else if (mLine.trim().toLowerCase().contains("coro")) {
                        verseType.setChorus(IndYesNoEnum.YES.getCodigo());
                    } else {
                        PhraseType phraseType = new PhraseType();
                        String phrase = mLine.replace("-", "").trim();

                        //Sempre que a frase iniciar com "-' significa que não terá
                        // repetição para a linha e sim para a estrofe
                        if (mLine.startsWith("-")) {
                            repeatVerse = true;
                        }

                        if (mLine.contains("(Bis)")) {
                            if (repeatVerse) {
                                verseType.setIndBis(IndYesNoEnum.YES.getCodigo());
                            } else {
                                phraseType.setIndBis(IndYesNoEnum.YES.getCodigo());
                            }
                            phrase = phrase.replace("(Bis)", "");
                        } else {
                            for (short i = 1; i < 6; i++) {
                                String xRepeat = "(" + i + "x)";
                                if (mLine.contains(xRepeat)) {
                                    if (repeatVerse) {
                                        verseType.setxRepeat(i);
                                    } else {
                                        phraseType.setxRepeat(i);
                                    }
                                    phrase = phrase.replace(xRepeat, "");
                                    break;
                                }
                            }
                        }

                        if (mLine.contains("(W)") || mLine.contains("(M)")) {
                            phraseType.setIndSing(IndSingEnum.WOMAN.getCodigo());
                            phrase = phrase.replace("(W)", "").replace("(M)", "");
                        } else if (mLine.contains("(M)") || mLine.contains("(H)")) {
                            phraseType.setIndSing(IndSingEnum.MAN.getCodigo());
                            phrase = phrase.replace("(M)", "").replace("(H)", "");
                        } else if (mLine.contains("(All)") || mLine.contains("(Todos)")) {
                            phraseType.setIndSing(IndSingEnum.ALL.getCodigo());
                            phrase = phrase.replace("(All)", "").replace("(Todos)", "");
                        }


                        phraseType.setPhrase(phrase);
                        verseType.addPhrase(phraseType);
                    }
                }
            }

            if (verseType != null) {
                songType.addVerse(verseType);

                SongRN songRN = new SongRN(getBaseContext());

                songRN.createNewSong(songType);
            }


        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.song_list_context, menu);

            if (mPlaylist != null) {
                menu.removeItem(R.id.mnuAddToPlaylist);
            } else {
                menu.removeItem(R.id.mnuRemoveFromPlaylist);
            }

            menu.setHeaderTitle(getString(R.string.listview_menu_title));

        } catch (Exception exc) {

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            SongType song = (SongType) mLstView.getItemAtPosition(info.position);

            switch (item.getItemId()) {
                case R.id.mnuAddToPlaylist:
                    Intent it = new Intent(getBaseContext(), PlaylistDialogActivity.class);
                    it.putExtra("idSong", song.getId());
                    startActivityForResult(it, ADD_SONG_TO_PLAYLIST_RC);
                    return true;

                case R.id.mnuRemoveFromPlaylist:
                    if (mPlaylist != null) {
                        if (mPlaylistRN.removeSongFromPlaylist(song, mPlaylist)) {
                            refresh();
                        }
                    }
                    return true;
            }

        } catch (Exception exc) {

        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, "Música adicionada a playlist", Toast.LENGTH_SHORT).show();
        }
    }

    private class ListViewListenerImpl implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            SongType songType = (SongType) adapter.getItemAtPosition(position);
            Intent it = new Intent(getBaseContext(), SongViewActivity.class);
            it.putExtra("idSong", songType.getId());
            if (mPlaylist != null) {
                it.putExtra("idPlaylist", mPlaylist.getId());
            }
            startActivity(it);
        }
    }

    private class TextWatcherImpl implements TextWatcher {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mAdapter.getFilter().filter(s);
        }
    }

}


