package com.techbeloved.journalbeloved.notes;

import android.support.annotation.NonNull;
import android.util.Log;

import com.techbeloved.journalbeloved.interfaces.FirebaseCallbacks;
import com.techbeloved.journalbeloved.model.Note;
import com.techbeloved.journalbeloved.util.FirebaseManager;

import java.util.List;

public class NotesPresenter implements NotesContract.Presenter,FirebaseCallbacks {

    private static final String TAG = NotesPresenter.class.getSimpleName();

    private final NotesContract.View mNotesView;

    private String mUserId;

    public NotesPresenter(NotesContract.View notesView) {
        this.mNotesView = notesView;
    }

    @Override
    public void start() {
        Log.i(TAG, "start: Starting to load data");
        loadNotes(false);
    }

    @Override
    public void onResume() {
        if (mNotesView != null) {
            mNotesView.showProgress();
            mUserId = mNotesView.getUserId();

            // Load the notes from firebase
            FirebaseManager.getInstance(mUserId, this).loadNotes();
        }
    }

    @Override
    public void onDestroy() {
        FirebaseManager.getInstance(mUserId, this).destroy();
    }

    @Override
    public void onNotesLoaded(List<Note> notes) {
        if (mNotesView != null) {
            mNotesView.showNotes(notes);
            mNotesView.hideProgress();
        }
    }

    // This is used only in the detail view
    @Override
    public void onNoteLoaded(Note note) {

    }

    @Override
    public void onNewNote(Note note) {

    }

    @Override
    public void onDataChanged(List<Note> currentNotes) {
        if (mNotesView != null) {
            // maybe use refresh notes
            mNotesView.showNotes(currentNotes);
            mNotesView.hideProgress();
        }
    }

    @Override
    public void onCancelled(String message) {

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadNotes(boolean forceUpdate) {
        if (forceUpdate) {
            FirebaseManager.getInstance(mUserId, this).refreshNotes();
        }
    }

    @Override
    public void addNewNote() {
        mNotesView.showAddNote();
    }

    @Override
    public void openNoteDetails(@NonNull Note requestedNote) {

    }

    @Override
    public void onItemClicked(String noteId) {
        mNotesView.showNoteDetailUi(noteId);
    }

    public NotesContract.View getNotesView() {
        return mNotesView;
    }
}
