package com.techbeloved.journalbeloved.notedetaileditadd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.techbeloved.journalbeloved.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddEditDetailNoteFragment extends Fragment implements AddEditDetailNoteContract.View {

    public static final String TAG = AddEditDetailNoteFragment.class.getSimpleName();

    public static final String ARGUMENT_EDIT_NOTE_ID = "EDIT_TASK_ID";

    private static final int REQUEST_EDIT_NOTE = 824;

    private static final int REQUEST_DETAIL_NOTE = 930;

    private AddEditDetailNoteContract.Presenter mPresenter;

    private EditText mTitle;

    private EditText mDetail;

    public static AddEditDetailNoteFragment newInstance() {
        return new AddEditDetailNoteFragment();
    }

    public AddEditDetailNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        Log.i(TAG, "onResume: AddEdit is here");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notedetail_frag, container, false);

        setHasOptionsMenu(true);
        mTitle = root.findViewById(R.id.edit_note_title);
        mDetail = root.findViewById(R.id.edit_note_detail);
        Log.i(TAG, "onCreateView: " + mTitle + ":  " + root.toString());

        return root;
    }
    

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mTitle.setText("");
            mDetail.setText("LOADING");
        }
    }

    @Override
    public void showEmptyNoteError() {

    }

    @Override
    public void showNotesList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
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
        return isAdded();
    }

    @Override
    public void setPresenter(AddEditDetailNoteContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO: 6/29/18 only save if note is edited
                mPresenter.saveNote(mTitle.getText().toString(), mDetail.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
