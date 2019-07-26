package at.tewan.androidmapper.mainmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import at.tewan.androidmapper.BeatmapPropertiesActivity;
import at.tewan.androidmapper.R;
import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.beatmap.info.Info;

/**
 * The ListView adapter seen in the main menu
 *
 * */
public class BeatmapListAdapter extends RecyclerView.Adapter<BeatmapListAdapter.BeatmapListAdapterHolder> {

    private Context context;
    private ArrayList<Info> items;

    public BeatmapListAdapter(Context context, ArrayList<Info> items) {
        this.context = context;
        this.items = items;
    }

    class BeatmapListAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView icon;
        private TextView songName, bpm;

        private int position;

        private BeatmapListAdapterHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.index_icon);
            songName = itemView.findViewById(R.id.index_songName);
            bpm = itemView.findViewById(R.id.index_bpm);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, BeatmapPropertiesActivity.class);

            intent.putExtra("beatmap_hash", items.get(position).getSongName().hashCode() + "");

            context.startActivity(intent);
        }
    }

    // Boring overrides down bellow

    @NonNull
    @Override
    public BeatmapListAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_beatmap, viewGroup, false);

        return new BeatmapListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeatmapListAdapterHolder holder, int i) {
        Info info = items.get(i);
        holder.songName.setText(info.getSongName());
        holder.bpm.setText(String.valueOf(info.getBeatsPerMinute()));

        File coverFile = new File(Beatmaps.BEATMAPS_ROOT, info.getSongName().hashCode() + System.getProperty("file.separator") + info.getCoverImageFilename());
        Bitmap bitmap = BitmapFactory.decodeFile(coverFile.toString());
        holder.icon.setImageBitmap(bitmap);

        holder.setPosition(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
