package com.techbeloved.journalbeloved.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.techbeloved.journalbeloved.Injection;
import com.techbeloved.journalbeloved.R;
import com.techbeloved.journalbeloved.util.ActivityUtils;

public class NotesActivity extends AppCompatActivity {

    private NotesPresenter mNotesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NotesFragment notesFragment =
                (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (notesFragment == null) {
            // Create fragment
            notesFragment = NotesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), notesFragment, R.id.contentFrame
            );
        }

        // Create the presenter
        mNotesPresenter = new NotesPresenter(
                Injection.provideNotesRepository(getApplicationContext()), notesFragment);


        // Setup sample data
//        List<Note> sampleNotes = new ArrayList<>();
//        sampleNotes.add(new Note("I want to go heaven", "Heaven is a beautiful and a wonderful place which no man should miss"));
//        sampleNotes.add(new Note("To God be the glory", "Great things He has done, so loved He the world and gave His Son"));
//        sampleNotes.add(new Note("Thousand tongue", "Oh for a thousand tongue to sing, my great redeemer's praise. The glories of my God and King"));
//        showNotes(sampleNotes);

    }

}
