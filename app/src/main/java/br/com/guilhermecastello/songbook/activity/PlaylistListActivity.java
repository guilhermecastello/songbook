package br.com.guilhermecastello.songbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.adapter.PlaylistAdapter;
import br.com.guilhermecastello.songbook.adapter.SongAdapter;
import br.com.guilhermecastello.songbook.rnbd.PlaylistRN;
import br.com.guilhermecastello.songbook.rnbd.SongRN;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;

public class PlaylistListActivity extends BaseActivity {

    private final int CREATE_PLAYLIST_RC = 1;

    private ListView mLstView = null;
    private TextView mEmptyView = null;
    private EditText mTxtSearch = null;

    private PlaylistRN mPlaylistRN = null;
    private PlaylistAdapter mAdapter = null;

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
        return R.id.nav_playlist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_list_activity);

        mLstView = (ListView) findViewById(android.R.id.list);
        mEmptyView = (TextView) findViewById(android.R.id.empty);
        mTxtSearch = (EditText) findViewById(R.id.txtSearch);

        mLstView.setOnItemClickListener(new ListViewListenerImpl());
        registerForContextMenu(mLstView);

        mTxtSearch.addTextChangedListener(new TextWatcherImpl());

        mPlaylistRN = new PlaylistRN(getBaseContext());

        refresh();
    }

    public void refresh() {
        mAdapter = new PlaylistAdapter(getBaseContext(), mPlaylistRN.list());
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
        // Cria Menu de Opções
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.playlist_list_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            // Trata eventos do Menu de Opções
            switch (item.getItemId()) {
                case R.id.mnuNewPlaylist:
                    Intent it = new Intent(getBaseContext(), PlaylistNewActivity.class);
                    startActivityForResult(it, CREATE_PLAYLIST_RC);
                    return true;
            }
        } catch (Exception exc) {

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.playlist_list_context, menu);

            menu.setHeaderTitle(getString(R.string.listview_menu_title));

        } catch (Exception exc) {

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            PlaylistType playlist = (PlaylistType) mLstView.getItemAtPosition(info.position);

            switch (item.getItemId()) {
                case R.id.mnuRemovePlaylist:
                    if (playlist != null) {
                        if (mPlaylistRN.removePlaylist(playlist)) {
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
            refresh();
        }
    }


    private class ListViewListenerImpl implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            PlaylistType playlist = (PlaylistType) adapter.getItemAtPosition(position);
            Intent it = new Intent(getBaseContext(), SongListActivity.class);
            it.putExtra("idPlaylist", playlist.getId());
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
