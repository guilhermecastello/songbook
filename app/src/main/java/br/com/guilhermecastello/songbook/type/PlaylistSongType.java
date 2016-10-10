package br.com.guilhermecastello.songbook.type;

/**
 * Created by guilh on 10/4/2016.
 */

public class PlaylistSongType {
    private Long idSong;

    private Long idPlaylist;

    private Integer songOrder;

    public Long getIdSong() {
        return idSong;
    }

    public void setIdSong(Long idSong) {
        this.idSong = idSong;
    }

    public Long getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Long idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public Integer getSongOrder() {
        return songOrder;
    }

    public void setSongOrder(Integer songOrder) {
        this.songOrder = songOrder;
    }
}
