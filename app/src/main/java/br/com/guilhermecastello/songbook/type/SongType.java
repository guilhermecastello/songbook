package br.com.guilhermecastello.songbook.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class SongType extends BaseType {

    private Long id;

    private String name;

    private Integer number;

    private Short language;

    List<VerseType> verses;

    public SongType() {

    }

    public SongType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<VerseType> getVerses() {
        return verses;
    }

    public void setVerses(List<VerseType> verses) {
        this.verses = verses;
    }

    public Short getLanguage() {
        return language;
    }

    public void setLanguage(Short language) {
        this.language = language;
    }

    public void addVerse(VerseType verseType) {
        if(this.verses == null) {
            this.verses = new ArrayList<VerseType>();
        }

        this.verses.add(verseType);
    }
}
