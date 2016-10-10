package br.com.guilhermecastello.songbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Filter;


import java.util.ArrayList;
import java.util.List;

import br.com.guilhermecastello.songbook.type.SongType;

/**
 * Created by guilherme-castello on 27/09/2016.
 */

public class SongAdapter extends BaseAdapter implements Filterable {

    private final Context context;

    private List<SongType> list = null;
    private List<SongType> fullList = null;

    static class ViewHolder {
        protected TextView text1;
    }

    public SongAdapter(Context ctx, List<SongType> list) {
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
            view = layout.inflate(android.R.layout.simple_list_item_1, null);
            viewHolder = new ViewHolder();
            viewHolder.text1 = (TextView) view.findViewById(android.R.id.text1);

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

        viewHolder.text1.setText(songName);

        return view;
    }

    public Filter getFilter() {

        return new Filter() {
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<SongType>) results.values;
                notifyDataSetChanged();
            }

            protected FilterResults performFiltering(CharSequence constraint) {
                List<SongType> filteredResults = getFilteredResults(constraint);
                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }
        };
    }

    private List<SongType> getFilteredResults(CharSequence constraint) {
        List<SongType> list = new ArrayList<SongType>();

        if (constraint == null || constraint.equals("")) {
            list = fullList;
        } else {
            for (SongType songType : fullList) {
                String item = songType.getNumber() + " - " + songType.getName();

                if (item.toUpperCase().contains(constraint.toString().toUpperCase())) {
                    list.add(songType);
                }
            } // End For
        }
        return list;
    }

}
