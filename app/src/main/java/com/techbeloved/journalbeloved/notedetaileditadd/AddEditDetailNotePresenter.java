package com.techbeloved.journalbeloved.notedetaileditadd;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.techbeloved.journalbeloved.interfaces.FirebaseCallbacks;
import com.techbeloved.journalbeloved.model.Note;
import com.techbeloved.journalbeloved.util.FirebaseManager;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddEditDetailNotePresenter implements AddEditDetailNoteContract.Presenter, FirebaseCallbacks {


    @NonNull
    private final AddEditDetailNoteContract.View mAddNoteView;

    private final String mUserId;

    @Nullable
    private final String mNoteId;


    public AddEditDetailNotePresenter(String userId, @Nullable String noteId,
                                      @NonNull AddEditDetailNoteContract.View addNoteView) {
        this.mNoteId = noteId;
        this.mAddNoteView = checkNotNull(addNoteView);
        this.mAddNoteView.setPresenter(this);
        this.mUserId = userId;
    }

    @Override
    public void onResume() {
        if (mAddNoteView != null) {
            mAddNoteView.setPresenter(this);
        }
    }

    @Override
    public void saveNote(Note note) {
        FirebaseManager.getInstance(mUserId, this).saveNote(note);
    }

    @Override
    public void populateNote() {
        if (isNewNote()) {
            throw new RuntimeException("populateNote() was called but note is new");
        }
        FirebaseManager.getInstance(mUserId, this).loadNote(mNoteId);
    }

    @Override
    public boolean isDataMissing() {
        return mAddNoteView.isDataMissing();
    }

    @Override
    public void deleteNote() {
        FirebaseManager.getInstance(mUserId, this).deleteNote(mNoteId);
        mAddNoteView.showNotesList();
    }

    @Override
    public void start() {
        if (!isNewNote() && isDataMissing()) {
            populateNote();
        }
    }

    @Override
    public void onDestroy() {
        FirebaseManager.getInstance(mUserId, this).destroy();
    }

    private boolean isNewNote() {
        return mNoteId == null;
    }

    private void createNote(String title, String detail){
        Note newNote = new Note(title, detail);
        if (TextUtils.isEmpty(newNote.getDetail()) && TextUtils.isEmpty(newNote.getTitle())) {
            mAddNoteView.showEmptyNoteError();
        } else {
            FirebaseManager.getInstance(mUserId, this).saveNote(newNote);
            mAddNoteView.showNotesList();
        }

    }

    private void updateNote(String title, String detail) {
        if (isNewNote()) {
            throw new RuntimeException("updateNote() was called but note is new.");
        }
        mAddNoteView.showNotesList();
    }

    @Override
    public void onNewNote(com.techbeloved.journalbeloved.model.Note dataSnapshot) {

    }

    @Override
    public void onDataChanged(List<com.techbeloved.journalbeloved.model.Note> currentNotes) {

    }

    @Override
    public void onCancelled(String message) {

    }

    @Override
    public void onNotesLoaded(List<com.techbeloved.journalbeloved.model.Note> notes) {

    }

    @Override
    public void onNoteLoaded(Note note) {
        if (note != null) {
            mAddNoteView.setTitle(note.title);
            mAddNoteView.setDetail(note.detail);
        }
    }
}
