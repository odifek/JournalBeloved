package com.techbeloved.journalbeloved.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.techbeloved.journalbeloved.data.Note;

import java.util.List;

/**
 * Data Access Object for the notes table
 */

@Dao
public interface NotesDao {
    /**
     * Select all notes from the notes table.
     *
     * @return all notes
     */
    @Query("SELECT * FROM notes")
    List<Note> getNotes();

    /**
     * Select a note by id
     */
    @Query("SELECT * FROM notes WHERE entryid = :noteId")
    Note getNoteById(String noteId);

    /**
     * Insert a note in the database. If the note already exists, replace it
     *
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    /**
     * Update note
     * @param note note to be updated
     * @return the number of notes updated. This should always be 1
     */
    @Update
    int updateNote(Note note);

    /**
     * Delete a note by id
     * @param noteId the note id
     * @return the number of notes deleted. This should always be 1
     */
    @Query("DELETE FROM notes WHERE entryid == :noteId")
    int deleteNoteById(String noteId);

    /**
     * Delete all notes
     */
    @Query("DELETE FROM notes")
    void deleteNotes();
}
