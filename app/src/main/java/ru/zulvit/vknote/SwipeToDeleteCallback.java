package ru.zulvit.vknote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {
    private final AdapterRecyclerNotes mAdapter;
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

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Draw the red delete background
            int color = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.colorPrimary);
            ColorDrawable deleteBackground = new ColorDrawable(color);
            deleteBackground.setBounds(viewHolder.itemView.getRight() + (int) dX,
                    viewHolder.itemView.getTop(), viewHolder.itemView.getRight(),
                    viewHolder.itemView.getBottom());
            deleteBackground.draw(c);

            // Draw the delete icon
            Drawable deleteIcon = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.delete_sweep_24);
            assert deleteIcon != null;
            int intrinsicWidth = deleteIcon.getIntrinsicWidth();
            int intrinsicHeight = deleteIcon.getIntrinsicHeight();
            int deleteIconMargin = (viewHolder.itemView.getHeight() - intrinsicHeight) / 2;
            int deleteIconTop = viewHolder.itemView.getTop() +
                    (viewHolder.itemView.getHeight() - intrinsicHeight) / 2;
            int deleteIconBottom = deleteIconTop + intrinsicHeight;
            int deleteIconLeft = viewHolder.itemView.getRight() - deleteIconMargin - intrinsicWidth;
            int deleteIconRight = viewHolder.itemView.getRight() - deleteIconMargin;
            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
            deleteIcon.draw(c);
        }
    }
}
