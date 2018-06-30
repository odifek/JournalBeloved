package com.techbeloved.journalbeloved.notes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.techbeloved.journalbeloved.data.Note;
import com.techbeloved.journalbeloved.data.source.NotesDataSource;
import com.techbeloved.journalbeloved.data.source.NotesRepository;
import com.techbeloved.journalbeloved.notedetaileditadd.AddEditDetailNoteActivity;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class NotesPresenter implements NotesContract.Presenter {

    private static final String TAG = NotesPresenter.class.getSimpleName();

    private final NotesRepository mNotesRepository;
    private final NotesContract.View mNotesView;
    private boolean mFirstLoad = true;

    public NotesPresenter(NotesRepository notesRepository, NotesContract.View notesView) {
        mNotesRepository = checkNotNull(notesRepository, "notesrepository cannot be null");
        mNotesView = checkNotNull(notesView, "notesView cannot be null");

        mNotesView.setPresenter(this);
    }

    @Override
    public void start() {
        Log.i(TAG, "start: Starting to load data");
        loadNotes(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // COMPLETED: If a note was successfully added, show snackbar
        if (AddEditDetailNoteActivity.REQUEST_ADD_NOTE == requestCode && resultCode == Activity.RESULT_OK) {
            mNotesView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadNotes(boolean forceUpdate) {
        loadNotes(forceUpdate || mFirstLoad, true);
    }


    private void loadNotes(boolean forceUpdate, final boolean showLoadingUi) {
        if (showLoadingUi) {
            mNotesView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mNotesRepository.refreshNotes();
        }

        mNotesRepository.getNotes(new NotesDataSource.LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {

                List<Note> notesToShow = new ArrayList<>(notes);

                if (mNotesView.isActive()) {
                    return;
                }
                Log.i(TAG, "onNotesLoaded: notes has been loaded: " + notes.size());
                processNotes(notesToShow);
            }

            @Override
            public void onDataNotAvailable() {
                if (!mNotesView.isActive()) {
                    return;
                }
                mNotesView.showLoadingNotesError();
            }
        });
    }

    private void processNotes(List<Note> notes) {
        if (notes.isEmpty()) {
            // TODO: 6/29/18 processEmptyNotes :  Show a message indicating there are no notes
            Log.i(TAG, "processNotes: nothing to show");
        } else {
            // Show list of notes
            mNotesView.showNotes(notes);
        }
    }

    @Override
    public void addNewNote() {
        mNotesView.showAddNote();
    }

    @Override
    public void openNoteDetails(@NonNull Note requestedNote) {

    }
}
