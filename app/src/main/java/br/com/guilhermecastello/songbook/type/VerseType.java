package br.com.guilhermecastello.songbook.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class VerseType {

    private Long id;

    private Long idSong;

    private Byte chorus;

    private Byte indBis;

    private Short xRepeat;

    private Short typeVoice;

    private List<PhraseType> phrases;

    public VerseType() {
    }

    public VerseType(Byte chorus) {
        this.chorus = chorus;
    }

    public VerseType(Byte chorus, Byte indBis, Short xRepeat,Short typeVoice, List<PhraseType> list) {
        this.chorus = chorus;
        this.indBis = indBis;
        this.xRepeat = xRepeat;
        this.typeVoice = typeVoice;
        this.phrases = list;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSong() {
        return idSong;
    }

    public void setIdSong(Long idSong) {
        this.idSong = idSong;
    }

    public Byte getChorus() {
        return chorus;
    }

    public void setChorus(Byte corus) {
        this.chorus = corus;
    }

    public List<PhraseType> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<PhraseType> phrases) {
        this.phrases = phrases;
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

    public Short getTypeVoice() {
        return typeVoice;
    }

    public void setTypeVoice(Short typeVoice) {
        this.typeVoice = typeVoice;
    }

    public void addPhrase(PhraseType phraseType) {
        if (this.phrases == null) {
            phrases = new ArrayList<PhraseType>();
        }
        this.phrases.add(phraseType);
    }

}
