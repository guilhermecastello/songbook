package br.com.guilhermecastello.songbook.file;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.guilhermecastello.songbook.enumeration.IndSingEnum;
import br.com.guilhermecastello.songbook.enumeration.IndYesNoEnum;
import br.com.guilhermecastello.songbook.enumeration.LanguageEnum;
import br.com.guilhermecastello.songbook.exception.RNException;
import br.com.guilhermecastello.songbook.type.PhraseType;
import br.com.guilhermecastello.songbook.type.SongType;
import br.com.guilhermecastello.songbook.type.VerseType;


/**
 * Created by guilh on 10/10/2016.
 */

public class SongFile {

    private Context context;

    public SongFile(Context ctx) {
        this.context = ctx;
    }


    public List<SongType> readAllSongsToImport(boolean readFromAssets) {
        ArrayList<SongType> songs = new ArrayList<SongType>();
        SongType songType = null;
        String files[] = null;
        try {
            if (readFromAssets) {
                files = this.listFilesFromAssets();
            }
            else {
                files = this.listFilesHomeFolder();
            }

            for (String file : files) {
                songType = readSongFromFile(file, readFromAssets);
                songs.add(songType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return songs;
    }

    private String[] listFilesFromAssets() throws IOException {
        return this.context.getAssets().list("");
    }

    private String[] listFilesHomeFolder() throws IOException {
        return this.context.getAssets().list("");
    }

    private BufferedReader getReaderFromAsset(String nameArq) throws IOException {
        return new BufferedReader(new InputStreamReader(this.context.getAssets().open(nameArq)));
    }

    private BufferedReader getReaderFromHomeFolder(String nameArq) throws IOException {
        return new BufferedReader(new InputStreamReader(this.context.getAssets().open(nameArq)));
    }

    private SongType readSongFromFile(String nameArq, boolean fromAsset) throws IOException {
        BufferedReader reader = null;

        SongType songType = null;
        VerseType verseType = null;

        String mLine;
        boolean firstRow = true;
        try {

            if (fromAsset) {
                reader = getReaderFromAsset(nameArq);
            }else {
                reader = getReaderFromHomeFolder(nameArq);
            }

            while ((mLine = reader.readLine()) != null) {
                if (firstRow) {
                    firstRow = false;

                    songType = this.createNewSong(mLine);
                } else {
                    if (mLine.trim().equals("")) {
                        if (verseType != null) {
                            songType.addVerse(verseType);
                        }
                        verseType = new VerseType();
                    } else if (mLine.trim().toLowerCase().contains("coro")) {
                        verseType.setChorus(IndYesNoEnum.YES.getCodigo());
                    } else {
                        this.createNewPhrase(mLine, verseType);
                    }
                }

                if (verseType != null) {
                    songType.addVerse(verseType);
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return songType;
    }

    private SongType createNewSong(String line) {
        SongType songType = new SongType();

        String[] fields = line.split("-");
        if (fields.length == 2) {
            songType.setNumber(Integer.valueOf(fields[0].trim()));
            songType.setName(fields[1].trim());
            LanguageEnum languageEnum = LanguageEnum.getByLanguage(Locale.getDefault().getLanguage());
            if (languageEnum == null) {
                languageEnum = LanguageEnum.PORTUGUESE;
            }
            songType.setLanguage(languageEnum.getCodigo());
        } else if (fields.length == 3) {
            songType.setNumber(Integer.valueOf(fields[0].trim()));
            songType.setName(fields[1].trim());
            LanguageEnum languageEnum = LanguageEnum.getByLanguage(fields[2].trim());
            if (languageEnum == null) {
                languageEnum = LanguageEnum.PORTUGUESE;
            }
            songType.setLanguage(languageEnum.getCodigo());

        } else {
            throw new RNException("Arquivo fora do padrão");
        }

        return songType;
    }

    private void createNewPhrase(String line, VerseType verseType) {
        PhraseType phraseType = new PhraseType();
        boolean repeatVerse = false;

        String phrase = line.replace("-", "").trim();

        //Sempre que a frase iniciar com "-' significa que não terá
        // repetição para a linha e sim para a estrofe
        if (line.startsWith("-")) {
            repeatVerse = true;
        }

        if (line.contains("(Bis)")) {
            if (repeatVerse) {
                verseType.setIndBis(IndYesNoEnum.YES.getCodigo());
            } else {
                phraseType.setIndBis(IndYesNoEnum.YES.getCodigo());
            }
            phrase = phrase.replace("(Bis)", "");
        } else {
            for (short i = 1; i < 6; i++) {
                String xRepeat = "(" + i + "x)";
                if (line.contains(xRepeat)) {
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

        if (line.contains("(W)") || line.contains("(M)")) {
            phraseType.setIndSing(IndSingEnum.WOMAN.getCodigo());
            phrase = phrase.replace("(W)", "").replace("(M)", "");
        } else if (line.contains("(M)") || line.contains("(H)")) {
            phraseType.setIndSing(IndSingEnum.MAN.getCodigo());
            phrase = phrase.replace("(M)", "").replace("(H)", "");
        } else if (line.contains("(All)") || line.contains("(Todos)")) {
            phraseType.setIndSing(IndSingEnum.ALL.getCodigo());
            phrase = phrase.replace("(All)", "").replace("(Todos)", "");
        }

        phraseType.setPhrase(phrase);
        verseType.addPhrase(phraseType);
    }

}

