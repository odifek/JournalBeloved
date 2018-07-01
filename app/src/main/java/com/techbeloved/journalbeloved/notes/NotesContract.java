package com.techbeloved.journalbeloved.notes;

import android.support.annotation.NonNull;

import com.techbeloved.journalbeloved.BasePresenter;
import com.techbeloved.journalbeloved.BaseView;
import com.techbeloved.journalbeloved.model.Note;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter
 */
public interface NotesContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showNotes(List<Note> notes);

        void showAddNote();

        void showNoteDetailUi(String noteId);

        void showLoadingNotesError();

        void showNoNotes();

        void showProgress();

        void hideProgress();

        boolean isActive();

        void showSuccessfullySavedMessage();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadNotes(boolean forceUpdate);

        void addNewNote();

        void openNoteDetails(@NonNull Note requestedNote);

        void onItemClicked(String noteId);
    }
}
