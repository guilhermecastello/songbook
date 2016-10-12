package br.com.guilhermecastello.songbook.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.common.BitMatrix;

import br.com.guilhermecastello.songbook.exception.RNException;
import br.com.guilhermecastello.songbook.type.PhraseType;
import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.type.VerseType;

/**
 * Created by guilh on 10/11/2016.
 */

public class QRCodeUtil {

    private static final String FIELD_SEPARATOR = "%";
    private static final String OBJECT_SEPARATOR = ";";

    private static final String SONG_IDENTIFIER = "#";
    private static final String VERSE_IDENTIFIER = "@";
    private static final String PHRASE_IDENTIFIER = "&";

    private static final String DEFAULT_NULL_VALUE = "0";

    public static SongType readSongFromQRCode(String qrCode) {
        String[] objetos = qrCode.split(OBJECT_SEPARATOR);
        SongType songTypeVolta = new SongType();
        VerseType verseType = null;

        for (int i = 0; i < objetos.length; i++) {
            String[] attrs = objetos[i].split(FIELD_SEPARATOR);
            if (attrs[0].equals(SONG_IDENTIFIER)) {
                songTypeVolta.setNumber(Integer.valueOf(attrs[1]));
                songTypeVolta.setName(attrs[2]);
                songTypeVolta.setLanguage(Short.valueOf(attrs[3]));
            } else if (attrs[0].equals(VERSE_IDENTIFIER)) {
                if (verseType != null) {
                    songTypeVolta.addVerse(verseType);
                }

                verseType = new VerseType();
                if (attrs[1] != null && !attrs[1].trim().equals(DEFAULT_NULL_VALUE)) {
                    verseType.setChorus(Byte.valueOf(attrs[1]));
                }
                if (attrs[2] != null && !attrs[2].trim().equals(DEFAULT_NULL_VALUE)) {
                    verseType.setIndBis(Byte.valueOf(attrs[2]));
                }
                if (attrs[3] != null && !attrs[3].trim().equals(DEFAULT_NULL_VALUE)) {
                    verseType.setTypeVoice(Short.valueOf(attrs[3]));
                }
                if (attrs[4] != null && !attrs[4].trim().equals(DEFAULT_NULL_VALUE)) {
                    verseType.setxRepeat(Short.valueOf(attrs[4]));
                }

            } else if (attrs[0].equals(PHRASE_IDENTIFIER)) {
                PhraseType phraseType = new PhraseType();
                phraseType.setPhrase(attrs[1]);
                if (attrs[2] != null && !attrs[2].trim().equals(DEFAULT_NULL_VALUE)) {
                    phraseType.setIndSing(Byte.valueOf(attrs[2]));
                }
                if (attrs[3] != null && !attrs[3].trim().equals(DEFAULT_NULL_VALUE)) {
                    phraseType.setIndBis(Byte.valueOf(attrs[3]));
                }
                if (attrs[4] != null && !attrs[4].trim().equals(DEFAULT_NULL_VALUE)) {
                    phraseType.setxRepeat(Short.valueOf(attrs[4]));
                }
                verseType.addPhrase(phraseType);
            } else {
                throw new RNException("QR Code não é válido");
            }
        }

        if (verseType != null) {
            songTypeVolta.addVerse(verseType);
        }

        return songTypeVolta;
    }

    public static PlaylistType readPlaylistFromQRCode(String qrCode) {
        return null;
    }



    public static String createQRCode(SongType songType) {
        StringBuilder qrCode = new StringBuilder();
        qrCode.append(SONG_IDENTIFIER);
        qrCode.append(FIELD_SEPARATOR);
        qrCode.append(songType.getNumber());
        qrCode.append(FIELD_SEPARATOR);
        qrCode.append(songType.getName());
        qrCode.append(FIELD_SEPARATOR);
        qrCode.append(songType.getLanguage());
        qrCode.append(OBJECT_SEPARATOR);

        for (VerseType verseType : songType.getVerses()) {
            qrCode.append(VERSE_IDENTIFIER);
            qrCode.append(FIELD_SEPARATOR);
            if (verseType.getChorus() != null) {
                qrCode.append(verseType.getChorus());
            }
            else {
                qrCode.append(DEFAULT_NULL_VALUE);
            }
            qrCode.append(FIELD_SEPARATOR);
            if (verseType.getIndBis() != null) {
                qrCode.append(verseType.getIndBis());
            }
            else {
                qrCode.append(DEFAULT_NULL_VALUE);
            }
            qrCode.append(FIELD_SEPARATOR);
            if (verseType.getTypeVoice() != null) {
                qrCode.append(verseType.getTypeVoice());
            }
            else {
                qrCode.append(DEFAULT_NULL_VALUE);
            }
            qrCode.append(FIELD_SEPARATOR);
            if (verseType.getxRepeat() != null) {
                qrCode.append(verseType.getxRepeat());
            }
            else {
                qrCode.append(DEFAULT_NULL_VALUE);
            }
            qrCode.append(OBJECT_SEPARATOR);

            for (PhraseType phraseType : verseType.getPhrases()) {
                qrCode.append(PHRASE_IDENTIFIER);
                qrCode.append(FIELD_SEPARATOR);
                qrCode.append(phraseType.getPhrase());
                qrCode.append(FIELD_SEPARATOR);
                if (phraseType.getIndSing() != null) {
                    qrCode.append(phraseType.getIndSing());
                }
                else {
                    qrCode.append(DEFAULT_NULL_VALUE);
                }
                qrCode.append(FIELD_SEPARATOR);
                if (phraseType.getIndSing() != null) {
                    qrCode.append(phraseType.getIndBis());
                }
                else {
                    qrCode.append(DEFAULT_NULL_VALUE);
                }
                qrCode.append(FIELD_SEPARATOR);
                if (phraseType.getIndSing() != null) {
                    qrCode.append(phraseType.getxRepeat());
                }
                else {
                    qrCode.append(DEFAULT_NULL_VALUE);
                }
                qrCode.append(OBJECT_SEPARATOR);
            }
        }

        return qrCode.toString();
    }

    public static String createQRCode(PlaylistType playlistType) {
        return null;
    }

    public static Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}
