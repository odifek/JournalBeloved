package com.techbeloved.journalbeloved.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techbeloved.journalbeloved.interfaces.FirebaseCallbacks;
import com.techbeloved.journalbeloved.model.Note;

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager implements ValueEventListener {

    private volatile static FirebaseManager sFirebaseManager;
    private DatabaseReference mNoteReference;
    private FirebaseCallbacks mCallbacks;

    public static synchronized  FirebaseManager getInstance(
            String userId, FirebaseCallbacks callbacks) {
        if (sFirebaseManager == null) {
            synchronized (FirebaseManager.class) {
                sFirebaseManager = new FirebaseManager(userId, callbacks);
            }
        }
        return sFirebaseManager;
    }

    private FirebaseManager(String userId, FirebaseCallbacks callbacks) {
        mNoteReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(userId)
                .child(Constants.NODE_NOTES);
        this.mCallbacks = callbacks;
    }

    public void saveNote(Note note) {
        mNoteReference.child(note.getId()).setValue(note);
    }

    public void loadNotes() {
        // This loads the notes
        addNoteListeners();
    }

    public void refreshNotes() {
        removeListeners();
        addNoteListeners();
    }

    public void updateNote(Note note) {
        mNoteReference.child(note.getId()).setValue(note);
    }

    public void deleteNote(Note note) {
        mNoteReference.child(note.getId()).removeValue();
    }

    private void addNoteListeners() {
        mNoteReference.addValueEventListener(this);
    }

    public void removeListeners() {
        mNoteReference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List<Note> currentNotes = new ArrayList<>();
        for (DataSnapshot snapshot :
                dataSnapshot.getChildren()) {
            currentNotes.add(snapshot.getValue(Note.class));
        }
        mCallbacks.onDataChanged(currentNotes);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        mCallbacks.onCancelled(databaseError.getMessage());
    }

    public void destroy() {
        sFirebaseManager = null;
        mCallbacks = null;
    }
}
