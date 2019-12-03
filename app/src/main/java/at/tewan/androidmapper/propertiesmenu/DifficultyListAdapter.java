package at.tewan.androidmapper.propertiesmenu;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import at.tewan.androidmapper.EditorActivity;
import at.tewan.androidmapper.R;
import at.tewan.androidmapper.beatmap.info.Info;
import at.tewan.androidmapper.beatmap.info.InfoDifficulty;

import static at.tewan.androidmapper.util.ActivityArguments.BEATMAP_CONTAINER;
import static at.tewan.androidmapper.util.ActivityArguments.BEATMAP_DIFFICULTY;

/**
 * @author Stefan Heinz
 */
public class DifficultyListAdapter extends RecyclerView.Adapter<DifficultyListAdapter.DifficultyListAdapterHolder>  {

    private Context context;
    private ArrayList<InfoDifficulty> items;
    private Info info;
    private String container;

    public DifficultyListAdapter(Context context, ArrayList<InfoDifficulty> items, Info info, String container) {
        this.context = context;
        this.items = items;
        this.info = info;
        this.container = container;
    }

    class DifficultyListAdapterHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        private TextView difficultyName, noteJumpSpeed;

        private String filename;

        DifficultyListAdapterHolder(@NonNull View itemView) {
            super(itemView);
            difficultyName = itemView.findViewById(R.id.difficultyName);
            noteJumpSpeed = itemView.findViewById(R.id.difficultyNJS);

            itemView.setOnTouchListener(this);
        }

        void setFilename(String filename) {
            this.filename = filename;
        }



        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_UP) {

                v.performClick();

                Intent intent = new Intent(context, EditorActivity.class);

                intent.putExtra(BEATMAP_CONTAINER, container);
                intent.putExtra(BEATMAP_DIFFICULTY, filename);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

            }

            return true;
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

        holder.setFilename(infoDifficulty.getBeatmapFilename());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public InfoDifficulty getItem(int position) {
        return items.get(position);
    }


}
