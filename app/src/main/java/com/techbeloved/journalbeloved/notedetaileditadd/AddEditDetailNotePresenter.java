package com.techbeloved.journalbeloved.notedetaileditadd;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.techbeloved.journalbeloved.data.Note;
import com.techbeloved.journalbeloved.data.source.NotesDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddEditDetailNotePresenter implements AddEditDetailNoteContract.Presenter, NotesDataSource.GetNoteCallback {

    @NonNull
    private final NotesDataSource mNotesRepository;

    @NonNull
    private final AddEditDetailNoteContract.View mAddNoteView;

    @Nullable
    private String mNoteId;

    private boolean mIsDataMissing;

    public AddEditDetailNotePresenter(@Nullable String noteId, @NonNull NotesDataSource notesRepository,
                                      @NonNull AddEditDetailNoteContract.View addNoteView, boolean shouldLoadDataFromRepo) {
        mNoteId = noteId;
        mNotesRepository = checkNotNull(notesRepository);
        mAddNoteView = checkNotNull(addNoteView);
        mIsDataMissing = shouldLoadDataFromRepo;

        mAddNoteView.setPresenter(this);
    }

    @Override
    public void onNoteLoaded(Note note) {
        if (mAddNoteView.isActive()) {
            mAddNoteView.setTitle(note.getTitle());
            mAddNoteView.setDetail(note.getDetail());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        if (mAddNoteView.isActive()) {
            mAddNoteView.showEmptyNoteError();
        }
    }

    @Override
    public void saveNote(String title, String detail) {
        if (isNewNote()) {
            createNote(title, detail);
        } else {
            updateNote(title, detail);
        }
    }

    @Override
    public void populateNote() {
        if (isNewNote()) {
            throw new RuntimeException("populateNote() was called but note is new");
        }
        mNotesRepository.getNote(mNoteId, this);
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    @Override
    public void deleteNote() {

    }

    @Override
    public void start() {
        if (!isNewNote() && mIsDataMissing) {
            populateNote();
        }
    }

    public boolean isNewNote() {
        return mNoteId == null;
    }

    private void createNote(String title, String detail){
        Note newNote = new Note(title, detail);
        if (newNote.isEmpty()) {
            mAddNoteView.showEmptyNoteError();
        } else {
            mNotesRepository.saveNote(newNote);
            mAddNoteView.showNotesList();
        }
    }

    private void updateNote(String title, String detail) {
        if (isNewNote()) {
            throw new RuntimeException("updateNote() was called but note is new.");
        }
        mNotesRepository.saveNote(new Note(title, detail, mNoteId));
        mAddNoteView.showNotesList();
    }
}
