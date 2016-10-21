package br.com.guilhermecastello.songbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.guilhermecastello.songbook.R;
import br.com.guilhermecastello.songbook.type.SongType;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class SongImportAdapter extends BaseAdapter {

    private final Context context;

    private List<SongType> list = null;
    private List<SongType> fullList = null;

    static class ViewHolder {
        protected TextView txvSongAdSong;
        protected TextView txvSongAdResult;
        protected ImageView imgSongAdStatus;
    }

    public SongImportAdapter(Context ctx, List<SongType> list) {
        this.context = ctx;
        this.list = list;
        this.fullList = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getPosition(String name) {
        int position = 0;
        for (SongType song : list) {
            if (song.getName().equals(name)) {
                return position;
            }
            position++;
        }
        return -1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater layout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layout.inflate(R.layout.song_import_adapter, null);
            viewHolder = new ViewHolder();
            viewHolder.txvSongAdSong = (TextView) view.findViewById(R.id.txvSongAdSong);
            viewHolder.txvSongAdResult = (TextView) view.findViewById(R.id.txvSongAdResult);
            viewHolder.imgSongAdStatus = (ImageView) view.findViewById(R.id.imgSongAdStatus);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        viewHolder = (ViewHolder) view.getTag();
        SongType song = list.get(position);

        StringBuilder songName = new StringBuilder();
        songName.append(String.format("%03d",song.getNumber()));
        songName.append(" - ");
        songName.append(song.getName());

        viewHolder.txvSongAdSong.setText(songName);
        viewHolder.txvSongAdResult.setText(song.getResult());

        return view;
    }

}
