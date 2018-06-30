package com.techbeloved.journalbeloved.data.source;

import android.support.annotation.NonNull;

import com.techbeloved.journalbeloved.data.Note;

import java.util.List;

/**
 * Main entry pont for accessing notes data.
 * <p>
 *     For simplicity, only getNotes() and getNote() have callbacks.
 *     Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new note is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 * </p>
 */
public interface NotesDataSource {

    interface LoadNotesCallback {

        void onNotesLoaded(List<Note> notes);

        void onDataNotAvailable();
    }

    interface GetNoteCallback {

        void onNoteLoaded(Note note);

        void onDataNotAvailable();
    }

    void getNotes(@NonNull LoadNotesCallback callback);

    void getNote(@NonNull String noteId, @NonNull GetNoteCallback callback);

    void saveNote(@NonNull Note note);

    void refreshNotes();

    void deleteAllNotes();

    void deleteNote(@NonNull String noteId);
}
