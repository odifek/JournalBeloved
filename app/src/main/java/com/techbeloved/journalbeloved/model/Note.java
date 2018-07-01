package com.techbeloved.journalbeloved.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

@IgnoreExtraProperties
public class Note {

    public String id;

    public String title;

    public String detail;

    /**
     * Use this constructor to create a new {@link Note}
     *
     * @param title  title of the note
     * @param detail content of the note
     */
    public Note(@Nullable String title, @Nullable String detail) {
        this(title, detail, UUID.randomUUID().toString());
    }

    public Note() {
        this.title = "";
        this.id = UUID.randomUUID().toString();
        this.detail = "";
    }

    /**
     * Use this constructor to specify a Note if the Note already has an id (Copy of another note)
     *
     * @param title  title of {@link Note}
     * @param detail detail of {@link Note}
     * @param id     id of {@link Note}
     */
    public Note(@Nullable String title, @Nullable String detail, @NonNull String id) {
        this.id = id;
        this.title = title;
        this.detail = detail;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDetail() {
        return detail;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    public void setDetail(@Nullable String detail) {
        this.detail = detail;
    }
}
