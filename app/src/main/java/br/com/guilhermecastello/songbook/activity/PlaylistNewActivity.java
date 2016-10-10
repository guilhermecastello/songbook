package br.com.guilhermecastello.songbook.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.rnbd.PlaylistRN;
import br.com.guilhermecastello.songbook.type.PlaylistType;

public class PlaylistNewActivity extends AppCompatActivity {

    private EditText mTxtName;
    private Button mBtnCancel;
    private Button mBtnCreate;

    private PlaylistRN mPlaylistRN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_new_activity);

        mPlaylistRN = new PlaylistRN(getBaseContext());

        mTxtName = (EditText) findViewById(R.id.txtPlaylistName);
        mBtnCancel = (Button) findViewById(R.id.btnCancelPlylist);
        mBtnCreate = (Button) findViewById(R.id.btnCreatePlaylist);

        mTxtName.addTextChangedListener(new TextWatcherImpl());
        mBtnCreate.setOnClickListener(new OnClickListenerImpl());
        mBtnCancel.setOnClickListener(new OnClickListenerImpl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Cria Menu de Opções
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.playlist_new_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {


        } catch (Exception exc) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void createPlaylist() {
        PlaylistType playlistType = new PlaylistType();
        playlistType.setName(mTxtName.getText().toString());

        mPlaylistRN.insert(playlistType);

        setResult(RESULT_OK);
        finish();
    }

    private class OnClickListenerImpl implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnCreatePlaylist:
                    createPlaylist();
                    break;
                case R.id.btnCancelPlylist:
                    finish();
                    break;
            }
        }
    }

    private class TextWatcherImpl implements TextWatcher {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mTxtName.getText().toString().trim().equals("")) {
                mBtnCreate.setEnabled(false);
            } else {
                mBtnCreate.setEnabled(true);
            }
        }
    }


}

