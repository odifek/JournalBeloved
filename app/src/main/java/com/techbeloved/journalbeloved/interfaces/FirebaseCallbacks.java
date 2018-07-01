package com.techbeloved.journalbeloved.interfaces;

import com.google.firebase.database.DataSnapshot;
import com.techbeloved.journalbeloved.model.Note;

import java.util.List;

public interface FirebaseCallbacks {
    void onNewNote(Note dataSnapshot);

    void onDataChanged(List<Note> currentNotes);

    void onCancelled(String message);

    void onNotesLoaded(List<Note> notes);
}
