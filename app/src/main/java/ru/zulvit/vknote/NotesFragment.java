package ru.zulvit.vknote;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Notes> notesArrayList;
    private String[] notesHeading;
    private int[] imageResourceId;
    private RecyclerView recyclerView;

    public NotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        adapterRecyclerNotes.notifyDataSetChanged();
    }

    private void dataInitialize() {
        notesArrayList = new ArrayList<>();
        notesHeading = new String[]{
                "Test1",
                "Test2",
                "Test3",
                "Test4",
                "Test5",
                "Test6",
                "Test7",
                "Test8",
                "Test9",
                "Test01"};

        imageResourceId = new int[]{
                R.drawable.speaker_notes_24,
                R.drawable.speaker_notes_24,
                R.drawable.speaker_notes_24,
                R.drawable.speaker_notes_24,
                R.drawable.speaker_notes_24,
                R.drawable.speaker_notes_24,
                R.drawable.speaker_notes_24,
                R.drawable.speaker_notes_24,
                R.drawable.speaker_notes_24,
                R.drawable.speaker_notes_24};

        for (int i = 0; i < notesHeading.length; i++) {
            Notes notes = new Notes(notesHeading[i], imageResourceId[i]);
            notesArrayList.add(notes);
        }
    }
}
