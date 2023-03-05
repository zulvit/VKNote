package ru.zulvit.vknote;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRecyclerNotes extends RecyclerView.Adapter<AdapterRecyclerNotes.CustomViewHolder> {
    private final Context context;
    static ArrayList<Notes> notesArrayList;

    public AdapterRecyclerNotes(Context context, ArrayList<Notes> notesArrayList) {
        this.context = context;
        AdapterRecyclerNotes.notesArrayList = notesArrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            Notes notes = notesArrayList.get(position);
            holder.noteHeading.setText(notes.heading);
            holder.noteDescription.setText(notes.description);
        }
    }

    @Override
    public int getItemCount() {
        if (notesArrayList != null) {
            return notesArrayList.size();
        } else {
            return 0;
        }
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView noteHeading;
        TextView noteDescription;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            noteDescription = itemView.findViewById(R.id.tvFilename);
            noteHeading = itemView.findViewById(R.id.tvMeta);
            itemView.setOnClickListener(view -> {
                int adapterPosition = getAdapterPosition();
                Log.d(AdapterRecyclerNotes.class.getName(), "long click" + notesArrayList.get(adapterPosition));
                Intent intent = new Intent(itemView.getContext(), PlayerActivity.class);
                intent.putExtra("noteHeading", notesArrayList.get(adapterPosition).heading);
                intent.putExtra("noteDescription", notesArrayList.get(adapterPosition).description);
                itemView.getContext().startActivity(intent);
            });

            itemView.setOnLongClickListener(view -> {
                int adapterPosition = getAdapterPosition();
                Log.d(AdapterRecyclerNotes.class.getName(), "long click" + adapterPosition);
                return false;
            });
        }
    }
}
