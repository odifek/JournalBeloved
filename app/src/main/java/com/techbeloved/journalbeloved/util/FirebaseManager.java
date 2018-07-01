package com.techbeloved.journalbeloved.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private static final String TAG = FirebaseManager.class.getSimpleName();

    private volatile static FirebaseManager sFirebaseManager;
    private final DatabaseReference mNoteReference;
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
                .child(userId);
        this.mCallbacks = callbacks;
    }

    public void saveNote(Note note) {
        String noteId = mNoteReference.child(Constants.NODE_NOTES).push().getKey();
        note.setId(noteId);
        mNoteReference.child(Constants.NODE_NOTES).child(note.getId()).setValue(note);
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
        mNoteReference.child(Constants.NODE_NOTES).child(note.getId()).setValue(note);
    }

    public void deleteNote(String noteId) {
        mNoteReference.child(Constants.NODE_NOTES).child(noteId).removeValue();
    }

    private void addNoteListeners() {
        mNoteReference.child(Constants.NODE_NOTES).addValueEventListener(this);
    }

    private void removeListeners() {
        mNoteReference.child(Constants.NODE_NOTES).removeEventListener(this);
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
        removeListeners();
    }

    public void loadNote(String noteId) {
        mNoteReference.child(Constants.NODE_NOTES).child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCallbacks.onNoteLoaded(dataSnapshot.getValue(Note.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
