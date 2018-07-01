package com.techbeloved.journalbeloved;

import android.content.Context;
import android.support.annotation.NonNull;

import com.techbeloved.journalbeloved.data.source.NotesRepository;
import com.techbeloved.journalbeloved.data.source.local.JournalDatabase;
import com.techbeloved.journalbeloved.data.source.local.NotesLocalDataSource;
import com.techbeloved.journalbeloved.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

class Injection {

    public static NotesRepository provideNotesRepository(@NonNull Context context) {
        checkNotNull(context);
        JournalDatabase database = JournalDatabase.getInstance(context);
        return NotesRepository.getInstance(
                NotesLocalDataSource.getInstance(new AppExecutors(), database.notesDao()));
    }
}
