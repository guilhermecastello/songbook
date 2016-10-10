package br.com.guilhermecastello.songbook.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import br.com.guilhermecastello.songbook.exception.RNException;
import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.adapter.PlaylistAdapter;
import br.com.guilhermecastello.songbook.rnbd.PlaylistRN;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.util.Util;

public class PlaylistDialogActivity extends AppCompatActivity {


    private ListView mLstView = null;
    private TextView mEmptyView = null;
    private EditText mTxtSearch = null;

    private PlaylistRN mPlaylistRN = null;
    private PlaylistAdapter mAdapter = null;

    private Long mIdSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_dialog_activity);

        mLstView = (ListView) findViewById(android.R.id.list);
        mEmptyView = (TextView) findViewById(android.R.id.empty);
        mTxtSearch = (EditText) findViewById(R.id.txtSearch);

        mLstView.setOnItemClickListener(new ListViewListenerImpl());

        mPlaylistRN = new PlaylistRN(getBaseContext());

        Bundle param = getIntent().getExtras();

        if (param != null) {
            Long idSong = param.getLong("idSong", 0);
            if (idSong != 0) {
                mIdSong = idSong;
            }
        }

        refresh();
    }

    private void refresh() {
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

    private void addSongToPlaylist(PlaylistType playlist) {
        SongType songType = new SongType();
        songType.setId(mIdSong);
        try {
            mPlaylistRN.addSongToPlaylist(songType, playlist);
            setResult(RESULT_OK);
            finish();
        }
        catch (RNException e) {
            Util.showWarning(this, e.getMessage());
        }
    }

    private class ListViewListenerImpl implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            PlaylistType playlist = (PlaylistType) adapter.getItemAtPosition(position);
            addSongToPlaylist(playlist);
        }
    }

}
