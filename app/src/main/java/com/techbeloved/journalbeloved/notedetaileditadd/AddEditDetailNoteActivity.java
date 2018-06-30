package com.techbeloved.journalbeloved.notedetaileditadd;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.techbeloved.journalbeloved.Injection;
import com.techbeloved.journalbeloved.R;
import com.techbeloved.journalbeloved.util.ActivityUtils;

public class AddEditDetailNoteActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_NOTE = 560;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO";
    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_TO_BE_SHOWN";
    public static final int REQUEST_EDIT_NOTE = 123;

    private AddEditDetailNotePresenter mAddEditDetailNotePresenter;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        AddEditDetailNoteFragment addEditDetailNoteFragment =
                (AddEditDetailNoteFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);

        String noteId = getIntent().getStringExtra(AddEditDetailNoteFragment.ARGUMENT_EDIT_NOTE_ID);

        setToolbarTitle(noteId);

        if (addEditDetailNoteFragment == null) {
            addEditDetailNoteFragment = AddEditDetailNoteFragment.newInstance();

            if (getIntent().hasExtra(AddEditDetailNoteFragment.ARGUMENT_EDIT_NOTE_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(AddEditDetailNoteFragment.ARGUMENT_EDIT_NOTE_ID, noteId);
                addEditDetailNoteFragment.setArguments(bundle);
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditDetailNoteFragment, R.id.contentFrame);
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        mAddEditDetailNotePresenter = new AddEditDetailNotePresenter(
                noteId,
                Injection.provideNotesRepository(getApplicationContext()),
                addEditDetailNoteFragment,
                shouldLoadDataFromRepo
        );
    }

    private void setToolbarTitle(String noteId) {
        if (noteId == null) {
            mActionBar.setTitle(R.string.add_note);
        } else {
            mActionBar.setTitle(R.string.edit_note);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddEditDetailNotePresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
