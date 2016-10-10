package br.com.guilhermecastello.songbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.guilhermecastello.songbook.type.PlaylistType;
import br.com.guilhermecastello.songbook.type.SongType;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class PlaylistAdapter extends BaseAdapter implements Filterable {

    private final Context context;

    private List<PlaylistType> list = null;
    private List<PlaylistType> fullList = null;

    static class ViewHolder {
        protected TextView text1;
    }

    public PlaylistAdapter(Context ctx, List<PlaylistType> list) {
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
        for (PlaylistType playlist : list) {
            if (playlist.getName().equals(name)) {
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
            view = layout.inflate(android.R.layout.simple_list_item_1, null);
            viewHolder = new ViewHolder();
            viewHolder.text1 = (TextView) view.findViewById(android.R.id.text1);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        viewHolder = (ViewHolder) view.getTag();
        PlaylistType playlist = list.get(position);

        StringBuilder playlistName = new StringBuilder();
        //songName.append(String.format("%03d",song.getNumber()));
        //songName.append(" - ");
        playlistName.append(playlist.getName());

        viewHolder.text1.setText(playlistName);

        return view;
    }

    public Filter getFilter() {

        return new Filter() {
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<PlaylistType>) results.values;
                notifyDataSetChanged();
            }

            protected FilterResults performFiltering(CharSequence constraint) {
                List<PlaylistType> filteredResults = getFilteredResults(constraint);
                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }
        };
    }

    private List<PlaylistType> getFilteredResults(CharSequence constraint) {
        List<PlaylistType> list = new ArrayList<PlaylistType>();

        if (constraint == null || constraint.equals("")) {
            list = fullList;
        } else {
            for (PlaylistType playlist : fullList) {
                String item =  playlist.getName();

                if (item.toUpperCase().contains(constraint.toString().toUpperCase())) {
                    list.add(playlist);
                }
            } // End For
        }
        return list;
    }

}
