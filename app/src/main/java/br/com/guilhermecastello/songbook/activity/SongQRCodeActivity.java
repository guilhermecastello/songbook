package br.com.guilhermecastello.songbook.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.rnbd.SongRN;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.util.QRCodeUtil;

public class SongQRCodeActivity extends BaseActivity {

    private ImageView mImgQrCode;

    private SongRN songRN;

    private SongType songTypeOpened;

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
        setContentView(R.layout.song_qrcode_activity);

        mImgQrCode = (ImageView) findViewById(R.id.imgQrCode);

        songRN = new SongRN(getBaseContext());

        Bundle param = getIntent().getExtras();
        if (param != null) {
            Long idSong = param.getLong("idSong", 0);
            if (idSong != 0) {
                songTypeOpened = songRN.openSong(idSong);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        new AsyncCaller().execute();
    }

    private void showQRCode()  {
        try {
            QRCodeWriter writer = new QRCodeWriter();

            String qrCode = QRCodeUtil.createQRCode(songTypeOpened);

            BitMatrix matrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, 800, 800);

            mImgQrCode.setImageBitmap(QRCodeUtil.toBitmap(matrix));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(SongQRCodeActivity.this);
        Bitmap qrCodeImg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage(SongQRCodeActivity.this.getString(R.string.progress_dialog_qr_code));
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            try {
                QRCodeWriter writer = new QRCodeWriter();

                String qrCode = QRCodeUtil.createQRCode(songTypeOpened);

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
