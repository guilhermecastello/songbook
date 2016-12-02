package br.com.guilhermecastello.songbook.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.adapter.SongAdapter;
import br.com.guilhermecastello.songbook.rnbd.PlaylistRN;
import br.com.guilhermecastello.songbook.rnbd.SongRN;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.util.Util;

public class SongListActivity extends BaseActivity {



    private final int ADD_SONG_TO_PLAYLIST_RC = 1;

    private final int IMPORT_SONGS_RC = 2;

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


        mTxtSearch.addTextChangedListener(new TextWatcherImpl());
        mLstView.setOnItemClickListener(new ListViewListenerImpl());
        registerForContextMenu(mLstView);


        mSongRN = new SongRN(getBaseContext());
        mPlaylistRN = new PlaylistRN(getBaseContext());

        refresh();


    }

    private void refresh() {
        List<SongType> lista;

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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_list_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.mnuImportSongs) {
            startActivityForResult(new Intent(getApplicationContext(), SongImportActivity.class), IMPORT_SONGS_RC);
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
            Intent it;
            switch (item.getItemId()) {
                case R.id.mnuAddToPlaylist:
                    it = new Intent(getBaseContext(), PlaylistDialogActivity.class);
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

                case R.id.mnuShowQRCode:
                    it = new Intent(getBaseContext(), SongQRCodeActivity.class);
                    it.putExtra("idSong", song.getId());
                    startActivity(it);
                    return true;
            }

        } catch (Exception exc) {

        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_SONG_TO_PLAYLIST_RC) {
                Toast.makeText(this, "Música adicionada a playlist", Toast.LENGTH_SHORT).show();
            }
            else {
                if (requestCode == IMPORT_SONGS_RC) {
                    refresh();
                }
            }
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


