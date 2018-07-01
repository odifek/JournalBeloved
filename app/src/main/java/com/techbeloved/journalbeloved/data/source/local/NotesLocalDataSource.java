package com.techbeloved.journalbeloved.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.techbeloved.journalbeloved.data.Note;
import com.techbeloved.journalbeloved.data.source.NotesDataSource;
import com.techbeloved.journalbeloved.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class NotesLocalDataSource implements NotesDataSource {

    private static volatile NotesLocalDataSource INSTANCE;

    private final NotesDao mNotesDao;

    private final AppExecutors mAppExecutors;

    // Prevent direct instantiation
    private NotesLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull NotesDao notesDao) {
        mAppExecutors = appExecutors;
        mNotesDao = notesDao;
    }

    public static NotesLocalDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull NotesDao notesDao) {
        if (INSTANCE == null) {
            synchronized (NotesLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NotesLocalDataSource(appExecutors, notesDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadNotesCallback#onDataNotAvailable()} is fired if the database does not exist,
     * or the table is empty
     */
    @Override
    public void getNotes(@NonNull LoadNotesCallback callback) {
        Runnable runnable = () -> {
            final List<Note> notes = mNotesDao.getNotes();
            mAppExecutors.mainThread().execute(() -> {
                if (notes.isEmpty()) {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable();
                } else {
                    callback.onNotesLoaded(notes);
                }
            });
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getNote(@NonNull String noteId, @NonNull GetNoteCallback callback) {
        Runnable runnable = () -> {
            final Note note = mNotesDao.getNoteById(noteId);

            mAppExecutors.mainThread().execute(()->{
                if (note != null) {
                    callback.onNoteLoaded(note);
                } else {
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveNote(@NonNull Note note) {
        checkNotNull(note);
        Runnable saveRunnable = ()-> mNotesDao.insertNote(note);
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void refreshNotes() {
        // Not required because the {@link NotesRepository} handles the logic of refreshing the
        // notes from all the available data sources.
    }

    @Override
    public void deleteAllNotes() {
        Runnable deleteRunnable = () -> mNotesDao.deleteNotes();

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteNote(@NonNull final String noteId) {
        Runnable deleteRunnable = () -> mNotesDao.deleteNoteById(noteId);

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @VisibleForTesting
    static void clearInstance() { INSTANCE = null; }
}
