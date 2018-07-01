package com.techbeloved.journalbeloved.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techbeloved.journalbeloved.BuildConfig;
import com.techbeloved.journalbeloved.R;
import com.techbeloved.journalbeloved.model.Note;
import com.techbeloved.journalbeloved.model.User;
import com.techbeloved.journalbeloved.notedetaileditadd.AddEditDetailNoteActivity;

import java.util.Arrays;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements NotesAdapter.ListItemClickListener, NotesContract.View {

    private static final String TAG = NotesActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 628;
    private static final String ANONYMOUS = "anonymous";
    private NotesAdapter mListAdapter;
    private NotesContract.Presenter mPresenter;
    private FirebaseUser mFirebaseUser;
    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mFirebaseAuth;
    private String mUsername;
    private String mUserId;
    private boolean mUserAuthenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Authenticate user
        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        // IF not authenticated
        if (mFirebaseUser == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                            .build(),
                    RC_SIGN_IN);
        } else {
            // Go ahead with other things
            // Create or update the user entry in the database

            // Setup the UI
//            setupUi();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirebaseUser != null) {
            mUsername = mFirebaseUser.getDisplayName();
            mUserId = mFirebaseUser.getUid();
            setupUi();

            mPresenter = new NotesPresenter(this);
            mPresenter.onResume();
        } else { // Re attempt sign in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                            .build(),
                    RC_SIGN_IN);
        }
    }

    private void checkUserExistsInDb() {
        mFirebaseDatabase.child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    createOrUpdateUser(mUsername);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                // Initialize Firebase Auth
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseUser = mFirebaseAuth.getCurrentUser();

                mFirebaseInstance = FirebaseDatabase.getInstance();
                mFirebaseDatabase = mFirebaseInstance.getReference("users");

                Log.i(TAG, "onActivityResult: " + mFirebaseDatabase.getKey());
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    mUsername = mFirebaseUser.getDisplayName();
                    mUserId = mFirebaseUser.getUid();
                    checkUserExistsInDb();
                }
            }
        }
    }

    private void createOrUpdateUser(String name) {
        if (!TextUtils.isEmpty(name)) {
            User user = new User(name, mUserId);
            mFirebaseDatabase.child(mUserId).setValue(user);

            // Create a placeholder note.
            Note note = new Note("", "");
            String id = mFirebaseDatabase.child(mUserId).child("notes").push().getKey();
            note.setId(id);
            mFirebaseDatabase.child(mUserId).child("notes").child(id).setValue(note);
        }
    }

    private void setupUi() {
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up task view
        RecyclerView recyclerView = findViewById(R.id.notes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mListAdapter = new NotesAdapter(this, this);
        recyclerView.setAdapter(mListAdapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_task);

        fab.setOnClickListener((View view) -> {
            mPresenter.addNewNote();
            Log.i(TAG, "onCreateView: fab has been pressed");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                mFirebaseUser = null;
                mUsername = ANONYMOUS;
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(String noteId) {
        mPresenter.onItemClicked(noteId);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showNotes(List<Note> notes) {

    }

    @Override
    public void showAddNote() {
        Log.i(TAG, "showAddNote: has been called");
        Intent intent = new Intent(this, AddEditDetailNoteActivity.class);
        startActivityForResult(intent, AddEditDetailNoteActivity.REQUEST_ADD_NOTE);
    }

    @Override
    public void showNoteDetailUi(String noteId) {
        Intent intent = new Intent(this, AddEditDetailNoteActivity.class);
        intent.putExtra(AddEditDetailNoteActivity.EXTRA_NOTE_ID, noteId);
        startActivityForResult(intent, AddEditDetailNoteActivity.REQUEST_EDIT_NOTE);
    }

    @Override
    public void showLoadingNotesError() {

    }

    @Override
    public void showNoNotes() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void showSuccessfullySavedMessage() {

    }

    @Override
    public void setPresenter(NotesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public String getUserId() {
        return mUserId;
    }
}
