package br.com.guilhermecastello.songbook.type;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class PhraseType {

    private Long id;

    private Long idVerse;

    private String phrase;

    private Byte indSing;

    private Byte indBis;

    private Short xRepeat;

    public PhraseType() {

    }

    public PhraseType(String phrase)  {
        this.phrase = phrase;
    }

    public PhraseType(String phrase, Byte indSing, Byte indBis, Short xRepeat) {
        this.phrase = phrase;
        this.indSing = indSing;
        this.xRepeat = xRepeat;
        this.indBis = indBis;
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

    public Byte getIndBis() {
        return indBis;
    }

    public void setIndBis(Byte indBis) {
        this.indBis = indBis;
    }

    public Short getxRepeat() {
        return xRepeat;
    }

    public void setxRepeat(Short xRepeat) {
        this.xRepeat = xRepeat;
    }
}
