package ru.zulvit.vknote;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {
    private AdapterRecyclerNotes mAdapter;
    private String dirPath;

    public SwipeToDeleteCallback(AdapterRecyclerNotes mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        Notes notes = mAdapter.getItem(position);
        if (notes != null) {
            mAdapter.deleteItem(position);
            Context context = viewHolder.itemView.getContext();
            dirPath = context.getFilesDir().getAbsolutePath() + "/notes/";
            File file = new File(dirPath + notes.heading);
            boolean success = file.delete();
            if (success) {
                Log.d("deleting...", "success");
            } else {
                Log.d("deleting...", "err");
            }
        }
    }
}
