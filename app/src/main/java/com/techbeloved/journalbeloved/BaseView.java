package com.techbeloved.journalbeloved;

public interface BaseView<T> {
    void setPresenter(T presenter);

    String getUserId();
}
