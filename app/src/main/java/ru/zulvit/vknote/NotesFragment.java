package ru.zulvit.vknote;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

public class NotesFragment extends Fragment {
    private ArrayList<Notes> notesArrayList;
    private String[] notesHeading;
    private String[] notesTitle;
    private RecyclerView recyclerView;

    public NotesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataInitialize();
        recyclerView = view.findViewById(R.id.notesRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        AdapterRecyclerNotes adapterRecyclerNotes = new AdapterRecyclerNotes(getContext(), notesArrayList);
        recyclerView.setAdapter(adapterRecyclerNotes);

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(adapterRecyclerNotes);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapterRecyclerNotes.notifyDataSetChanged();
    }

    private void dataInitialize() {
        String path = getActivity().getFilesDir() + "/notes";
        Log.d(NotesFragment.class.getName(), path);
        File directory = new File(path);
        File[] allNotes = directory.listFiles();
        if (allNotes != null) {
            notesArrayList = new ArrayList<>();
            notesHeading = new String[allNotes.length];
            for (int i = 0; i < allNotes.length; i++) {
                Log.d(NotesFragment.class.getName(), allNotes[i].getName());
                notesHeading[i] = allNotes[i].getName();
            }

            notesTitle = new String[allNotes.length];
            for (int i = 0; i < allNotes.length; i++) {
                Log.d(NotesFragment.class.getName(), allNotes[i].getName());
                notesTitle[i] = allNotes[i].getName();
            }

            for (int i = notesHeading.length - 1; i >= 0; i--) {
                Notes notes = new Notes(notesHeading[i], notesTitle[i]);
                notesArrayList.add(notes);
            }
        }
    }
}
