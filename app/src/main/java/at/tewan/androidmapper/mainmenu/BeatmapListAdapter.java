package at.tewan.androidmapper.mainmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import at.tewan.androidmapper.R;
import at.tewan.androidmapper.beatmap.Beatmap;

/**
 * The ListView adapter seen in the main menu
 *
 * */
public class BeatmapListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Beatmap> items;

    public BeatmapListAdapter(Context context, ArrayList<Beatmap> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_beatmap, parent, false);

        Beatmap beatmap = getItem(position);

        ImageView icon = convertView.findViewById(R.id.icon);
        TextView title = convertView.findViewById(R.id.title);

        icon.setImageResource(R.drawable.android_cat_32);
        title.setText(beatmap.getSongName());




        return convertView;
    }

    // Boring overrides down bellow

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Beatmap getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



}
