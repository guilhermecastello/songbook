package br.com.guilhermecastello.songbook.type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilh on 10/4/2016.
 */

public class PlaylistType {

    private Long id;

    private String name;

    private List<SongType> songs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SongType> getSongs() {
        return songs;
    }

    public void setSongs(List<SongType> songs) {
        this.songs = songs;
    }

    public void addSong(SongType songType) {
        if(this.songs == null) {
            this.songs = new ArrayList<SongType>();
        }

        this.songs.add(songType);
    }
}
