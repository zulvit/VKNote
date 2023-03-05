package ru.zulvit.vknote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class AdapterRecyclerNotes extends RecyclerView.Adapter<AdapterRecyclerNotes.CustomViewHolder> {
    Context context;
    ArrayList<Notes> notesArrayList;

    public AdapterRecyclerNotes(Context context, ArrayList<Notes> notesArrayList) {
        this.context = context;
        this.notesArrayList = notesArrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Notes notes = notesArrayList.get(position);
        holder.noteHeading.setText(notes.heading);
        holder.titleImage.setImageResource(notes.titleImage);
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView titleImage;
        TextView noteHeading;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            titleImage = itemView.findViewById(R.id.title_image);
            noteHeading = itemView.findViewById(R.id.noteHeading);
        }
    }
}
