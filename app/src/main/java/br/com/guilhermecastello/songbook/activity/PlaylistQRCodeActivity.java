package br.com.guilhermecastello.songbook.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.rnbd.PlaylistRN;
import br.com.guilhermecastello.songbook.rnbd.SongRN;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.util.QRCodeUtil;

public class PlaylistQRCodeActivity extends BaseActivity {


    private ImageView mImgQrCode;

    private PlaylistRN playlistRN;

    private SongRN songRN;

    private PlaylistType playlistOpened;

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
        setContentView(R.layout.playlist_qrcode_activity);

        mImgQrCode = (ImageView) findViewById(R.id.imgQrCode);

        playlistRN = new PlaylistRN(getBaseContext());
        songRN = new SongRN(getBaseContext());

        Bundle param = getIntent().getExtras();
        if (param != null) {
            Long idPlaylist = param.getLong("idPlaylist", 0);
            if (idPlaylist != 0) {
                playlistOpened  = playlistRN.get(idPlaylist);
                List<SongType> songs = songRN.listPlaylist(playlistOpened);
                playlistOpened.setSongs(songs);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        new AsyncCaller().execute();
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(PlaylistQRCodeActivity.this);
        Bitmap qrCodeImg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage(PlaylistQRCodeActivity.this.getString(R.string.progress_dialog_qr_code));
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            try {
                QRCodeWriter writer = new QRCodeWriter();

                String qrCode = QRCodeUtil.createQRCode(playlistOpened);

                BitMatrix matrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, 800, 800);

                qrCodeImg = QRCodeUtil.toBitmap(matrix);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            mImgQrCode.setImageBitmap(qrCodeImg);
            pdLoading.dismiss();
        }

    }

}
