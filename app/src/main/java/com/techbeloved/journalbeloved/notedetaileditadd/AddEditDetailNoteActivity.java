package com.techbeloved.journalbeloved.notedetaileditadd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


import com.techbeloved.journalbeloved.R;
import com.techbeloved.journalbeloved.model.Note;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddEditDetailNoteActivity extends AppCompatActivity implements AddEditDetailNoteContract.View {

    public static final int REQUEST_ADD_NOTE = 560;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO";
    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_TO_BE_SHOWN";
    public static final int REQUEST_EDIT_NOTE = 123;

    private static final String CURRENT_USER_ID = "CurrentUserId";

    private AddEditDetailNoteContract.Presenter mPresenter;

    private ActionBar mActionBar;

    private String mNoteId;

    private String mUserId;

    private EditText mTitle;

    private EditText mDetail;

    private boolean mNoteEdited = false;

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mNoteEdited = true;
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        mTitle = findViewById(R.id.edit_note_title);
        mDetail = findViewById(R.id.edit_note_detail);

        // add text watcher listener
        mTitle.addTextChangedListener(mTextWatcher);
        mDetail.addTextChangedListener(mTextWatcher);

        Intent intent = getIntent();
        if (intent.hasExtra(CURRENT_USER_ID)) {
            mUserId = intent.getStringExtra(CURRENT_USER_ID);
        }
        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            mNoteId = intent.getStringExtra(EXTRA_NOTE_ID);
        }

        setToolbarTitle(mNoteId);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter = new AddEditDetailNotePresenter(mUserId, mNoteId, this);
        mPresenter.onResume();
        mPresenter.start();
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
//        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddEditDetailNotePresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // COMPLETED: 6/29/18 only save if note is edited
                if (mNoteEdited) {
                    Note newNote = new Note(
                            mTitle.getText().toString(),
                            mDetail.getText().toString());
                    mPresenter.saveNote(newNote);
                }
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showEmptyNoteError() {

    }

    @Override
    public void showNotesList() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setDetail(String detail) {
        mDetail.setText(detail);
    }

    @Override
    public void showNoteDeleted() {

    }

    @Override
    public void showTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void showDetail(String detail) {
        mDetail.setText(detail);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean isDataMissing() {
        return TextUtils.isEmpty(mDetail.getText().toString()) && TextUtils.isEmpty(mTitle.getText().toString());
    }

    @Override
    public void setPresenter(AddEditDetailNoteContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public String getUserId() {
        return null;
    }
}
