package com.techbeloved.journalbeloved.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.techbeloved.journalbeloved.data.Note;

/**
 * The {@link android.arch.persistence.room.RoomDatabase} Database that contains
 */
@Database(entities = {Note.class}, version = 1)
public abstract class JournalDatabase extends RoomDatabase {

    private static JournalDatabase INSTANCE;

    public abstract NotesDao notesDao();

    private static final Object sLock = new Object();

    public static JournalDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        JournalDatabase.class, "notes.db")
                        .build();
            }

            return INSTANCE;
        }
    }
}
