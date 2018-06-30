package com.techbeloved.journalbeloved.notedetaileditadd;

import com.techbeloved.journalbeloved.BasePresenter;
import com.techbeloved.journalbeloved.BaseView;

public interface AddEditDetailNoteContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showEmptyNoteError();

        // Return to the notes list
        void showNotesList();

        void setTitle(String title);

        void setDetail(String detail);

        void showNoteDeleted();

        void showTitle(String title);

        void showDetail(String detail);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveNote(String title, String detail);

        void populateNote();

        boolean isDataMissing();

        void deleteNote();
    }
}
