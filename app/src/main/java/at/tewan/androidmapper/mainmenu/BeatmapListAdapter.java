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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import at.tewan.androidmapper.BeatmapPropertiesActivity;
import at.tewan.androidmapper.R;
import at.tewan.androidmapper.beatmap.Beatmap;
import at.tewan.androidmapper.beatmap.Beatmaps;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.preferences.Preferences;
import at.tewan.androidmapper.util.ErrorPrinter;

import static at.tewan.androidmapper.util.ActivityArguments.BEATMAP_CONTAINER;

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

        // Set dark theme
        if(Preferences.isDarkTheme()) {
            context.setTheme(R.style.AppTheme_Dark);
        }
    }

    class BeatmapListAdapterHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        private ImageView icon;
        private TextView songName, bpm;

        private int position;

        private BeatmapListAdapterHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.index_icon);
            songName = itemView.findViewById(R.id.index_songName);
            bpm = itemView.findViewById(R.id.index_bpm);

            itemView.setOnTouchListener(this);
        }

        void setPosition(int position) {
            this.position = position;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_UP) {

                v.performClick();

                Intent intent = new Intent(context, BeatmapPropertiesActivity.class);

                intent.putExtra(BEATMAP_CONTAINER, getContainer(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

            }

            return true;
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

        // Resulting string example: 140.0 BPM
        String bpm = info.getBeatsPerMinute() + " " + context.getResources().getString(R.string.bpm);
        holder.bpm.setText(bpm);

        // Load cover image
        Bitmap bitmap = Beatmaps.getCoverBitmap(context, Beatmaps.getCover(getContainer(i), info.getCoverImageFilename()));
        holder.icon.setImageBitmap(bitmap);

        holder.setPosition(i);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String getContainer(int position) {
        return Beatmaps.getContainers()[position].getName();
    }
}
