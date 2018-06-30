package com.techbeloved.journalbeloved.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Immutable model class for a Note
 */
@Entity(tableName = "notes")
public final class Note {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private String id;

    @Nullable
    @ColumnInfo(name = "title")
    private String title;

    @Nullable
    @ColumnInfo(name = "detail")
    private String detail;

    /**
     * Use this constructor to create a new {@link Note}
     * @param title  title of the note
     * @param detail  content of the note
     */
    @Ignore
    public Note(@Nullable String title, @Nullable String detail) {
        this(title, detail, UUID.randomUUID().toString());
    }

    public Note(){
        this.title = "";
        this.id = UUID.randomUUID().toString();
        this.detail = "";
    }

    /**
     * Use this constructor to specify a Note if the Note already has an id (Copy of another note)
     * @param title title of {@link Note}
     * @param detail detail of {@link Note}
     * @param id id of {@link Note}
     */
    @Ignore
    public Note(@Nullable String title, @Nullable String detail, @NonNull String id) {
        this.id = id;
        this.title = title;
        this.detail = detail;
    }

    @NonNull
    public String getId() { return id; }

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

    /**
     * If note has no title, in list display some of the detail instead
     * @return either title of detail
     */
    @Nullable
    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(title)) {
            return title;
        } else {
            return detail;
        }
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(title) &&
                Strings.isNullOrEmpty(detail);
    }

    @Override
    public String toString() {
        return "Note with title " + title;
    }
}
