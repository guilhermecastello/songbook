package br.com.guilhermecastello.songbook.type;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class PhraseType {

    private Long id;

    private Long idVerse;

    private String phrase;

    private Byte indSing;


    public PhraseType() {

    }

    public PhraseType(String phrase)  {
        this.phrase = phrase;
    }

    public PhraseType(String phrase, Byte indSing) {
        this.phrase = phrase;
        this.indSing = indSing;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdVerse() {
        return idVerse;
    }

    public void setIdVerse(Long idVerse) {
        this.idVerse = idVerse;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public Byte getIndSing() {
        return indSing;
    }

    public void setIndSing(Byte indSing) {
        this.indSing = indSing;
    }
    
}
