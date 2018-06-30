package com.techbeloved.journalbeloved.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techbeloved.journalbeloved.R;
import com.techbeloved.journalbeloved.data.Note;
import com.techbeloved.journalbeloved.notedetaileditadd.AddEditDetailNoteActivity;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class NotesFragment extends Fragment implements NotesContract.View, NotesAdapter.ListItemClickListener {

    public static final String TAG = NotesFragment.class.getSimpleName();

    private NotesContract.Presenter  mPresenter;
    private NotesAdapter mListAdapter;

    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new NotesAdapter(getContext(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notes_frag, container, false);

        // Set up task view
        RecyclerView recyclerView = root.findViewById(R.id.notes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and attach it to the RecyclerView
        recyclerView.setAdapter(mListAdapter);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_task);

        fab.setOnClickListener((View view) -> {
            mPresenter.addNewNote();
            Log.i(TAG, "onCreateView: fab has been pressed");
        });

        return root;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showNotes(List<Note> notes) {
        mListAdapter.setNotes(notes);
    }

    @Override
    public void showAddNote() {
        Log.i(TAG, "showAddNote: has been called");
        Intent intent = new Intent(getContext(), AddEditDetailNoteActivity.class);
        startActivityForResult(intent, AddEditDetailNoteActivity.REQUEST_ADD_NOTE);
    }

    @Override
    public void showNoteDetailUi(String noteId) {
        Intent intent = new Intent(getContext(), AddEditDetailNoteActivity.class);
        intent.putExtra(AddEditDetailNoteActivity.EXTRA_NOTE_ID, noteId);
        startActivityForResult(intent, AddEditDetailNoteActivity.REQUEST_EDIT_NOTE);
    }

    @Override
    public void showLoadingNotesError() {

    }

    @Override
    public void showNoNotes() {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.note_saved));
    }

    @Override
    public void setPresenter(NotesContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onListItemClick(String noteId) {
        Intent intent = new Intent(getContext(), AddEditDetailNoteActivity.class);
        intent.putExtra(AddEditDetailNoteActivity.EXTRA_NOTE_ID, noteId);
        startActivityForResult(intent, AddEditDetailNoteActivity.REQUEST_EDIT_NOTE);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}
