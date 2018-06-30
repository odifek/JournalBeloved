package com.techbeloved.journalbeloved.data.source;

import android.support.annotation.NonNull;

import com.techbeloved.journalbeloved.data.Note;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 */
public class NotesRepository implements NotesDataSource {

    private static NotesRepository INSTANCE = null;

    private final NotesDataSource mNotesRemoteDataSource;

    private final NotesDataSource mNotesLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Note> mCachedNotes;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation
    private NotesRepository(@NonNull NotesDataSource notesRemoteDataSource,
                            @NonNull NotesDataSource notesLocalDataSource) {
        mNotesRemoteDataSource = checkNotNull(notesRemoteDataSource);
        mNotesLocalDataSource = checkNotNull(notesLocalDataSource);
    }

    private NotesRepository(@NonNull NotesDataSource notesLocaldataSource) {
        mNotesLocalDataSource = checkNotNull(notesLocaldataSource);
        // I have not configured remote data sources
        mNotesRemoteDataSource = null;
    }

    /**
     * Returns the single instance of this class, creating it if necessary
     * @param notesLocalDataSource the device storage data source
     * @return the {@link NotesRepository} instance
     */
    public static NotesRepository getInstance(NotesDataSource notesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new NotesRepository(notesLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(NotesDataSource)} to create a new instance next time it's called
     *
     */
    public static void destroyInstance() { INSTANCE = null; }

    @Override
    public void getNotes(@NonNull LoadNotesCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedNotes != null && !mCacheIsDirty) {
            callback.onNotesLoaded(new ArrayList<>(mCachedNotes.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we new to fetch new data from network
//            getNotesFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network
            mNotesLocalDataSource.getNotes(new LoadNotesCallback() {
                @Override
                public void onNotesLoaded(List<Note> notes) {
                    refreshCache(notes);
                    callback.onNotesLoaded(new ArrayList<>(mCachedNotes.values()));
                }

                @Override
                public void onDataNotAvailable() {
//TODO:                    getNotesFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void getNote(@NonNull String noteId, @NonNull GetNoteCallback callback) {
        checkNotNull(noteId);
        checkNotNull(callback);

        Note cachedNote = getNoteWithId(noteId);

        // Respond immediately with cache if available
        if (cachedNote != null) {
            callback.onNoteLoaded(cachedNote);
            return;
        }

        // Load from server/persisted if needed.

        // Is the note in the local data source? If not, query the network
        mNotesLocalDataSource.getNote(noteId, new GetNoteCallback() {
            @Override
            public void onNoteLoaded(Note note) {
                // do in memory cache update to keep the app UI up to date
                if (mCachedNotes == null) {
                    mCachedNotes = new LinkedHashMap<>();
                }
                mCachedNotes.put(note.getId(), note);
                callback.onNoteLoaded(note);
            }

            @Override
            public void onDataNotAvailable() {
                mNotesRemoteDataSource.getNote(noteId, new GetNoteCallback() {
                    @Override
                    public void onNoteLoaded(Note note) {
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedNotes == null) {
                            mCachedNotes = new LinkedHashMap<>();
                        }
                        mCachedNotes.put(note.getId(), note);
                        callback.onNoteLoaded(note);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveNote(@NonNull Note note) {
        checkNotNull(note);
//TODO:        mNotesRemoteDataSource.saveNote(note);
        mNotesLocalDataSource.saveNote(note);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedNotes == null) {
            mCachedNotes = new LinkedHashMap<>();
        }
        mCachedNotes.put(note.getId(), note);
    }

    @Override
    public void refreshNotes() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllNotes() {
//TODO:        mNotesRemoteDataSource.deleteAllNotes();
        mNotesLocalDataSource.deleteAllNotes();

        if (mCachedNotes == null) {
            mCachedNotes = new LinkedHashMap<>();
        }
        mCachedNotes.clear();
    }

    @Override
    public void deleteNote(@NonNull String noteId) {
//TODO:        mNotesRemoteDataSource.deleteNote(checkNotNull(noteId));
        mNotesLocalDataSource.deleteNote(checkNotNull(noteId));

        mCachedNotes.remove(noteId);
    }

    private Note getNoteWithId(String id) {
        checkNotNull(id);
        if (mCachedNotes == null || mCachedNotes.isEmpty()) {
            return null;
        } else {
            return mCachedNotes.get(id);
        }
    }

    private void refreshCache(List<Note> notes) {
        if (mCachedNotes == null) {
            mCachedNotes = new LinkedHashMap<>();
        }
        mCachedNotes.clear();
        for (Note note: notes) {
            mCachedNotes.put(note.getId(), note);
        }
        mCacheIsDirty = false;
    }

    private void getNotesFromRemoteDataSource(LoadNotesCallback callback) {
        mNotesRemoteDataSource.getNotes(new LoadNotesCallback() {
            @Override
            public void onNotesLoaded(List<Note> notes) {
                refreshCache(notes);
                refreshLocalDataSource(notes);
                callback.onNotesLoaded(new ArrayList<>(mCachedNotes.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<Note> notes) {
        mNotesLocalDataSource.deleteAllNotes();
        for (Note note: notes) {
            mNotesLocalDataSource.saveNote(note);
        }
    }

}
