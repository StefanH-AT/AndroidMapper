package at.tewan.androidmapper.propertiesmenu;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import at.tewan.androidmapper.BeatmapPropertiesActivity;
import at.tewan.androidmapper.EditorActivity;
import at.tewan.androidmapper.R;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.beatmap.info.InfoDifficulty;


public class DifficultyListAdapter extends RecyclerView.Adapter<DifficultyListAdapter.DifficultyListAdapterHolder>  {

    private Context context;
    private ArrayList<InfoDifficulty> items;
    private Info info;

    public DifficultyListAdapter(Context context, ArrayList<InfoDifficulty> items, Info info) {
        this.context = context;
        this.items = items;
        this.info = info;
    }

    class DifficultyListAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView difficultyName, noteJumpSpeed;

        private int position;

        public DifficultyListAdapterHolder(@NonNull View itemView) {
            super(itemView);
            difficultyName = itemView.findViewById(R.id.difficultyName);
            noteJumpSpeed = itemView.findViewById(R.id.difficultyNJS);

            itemView.setOnClickListener(this);
        }

        public void setPosition(int position) {
            this.position = position;
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, EditorActivity.class);

            intent.putExtra("beatmap_hash", info.getSongName().hashCode() + "");
            intent.putExtra("beatmap_difficulty_hash", getItem(position).hashCode() + "");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public DifficultyListAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_difficulty, viewGroup, false);

        return new DifficultyListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DifficultyListAdapterHolder holder, int i) {
        InfoDifficulty infoDifficulty = getItem(i);

        holder.noteJumpSpeed.setText(String.valueOf(infoDifficulty.getNoteJumpMovementSpeed()));
        holder.difficultyName.setText(infoDifficulty.getDifficulty());

        holder.setPosition(i);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public InfoDifficulty getItem(int position) {
        return items.get(position);
    }


}
