package com.example.rikharthu.contentproviderdemo.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;

import java.util.Date;

import static com.example.rikharthu.contentproviderdemo.data.models.Note.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Note {

    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_REMIND_AT = "remind_at";
    public static final String COLUMN_FINISHED = "is_finished";

    @ColumnInfo(name = COLUMN_ID)
    @PrimaryKey(autoGenerate = true)
    private long mId;
    @ColumnInfo(name = COLUMN_TITLE)
    private String mTitle;
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    private String mDescription;
    @ColumnInfo(name = COLUMN_CREATED_AT)
    private Date mCreatedAt;
    @ColumnInfo(name = COLUMN_REMIND_AT)
    private Date mRemindAt;
    @ColumnInfo(name = COLUMN_FINISHED)
    private boolean mFinished;

    public Note() {
    }

    @Ignore
    public Note(String title, String description, Date createdAt, Date remindAt, boolean finished) {
        mTitle = title;
        mDescription = description;
        mCreatedAt = createdAt;
        mRemindAt = remindAt;
        mFinished = finished;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public Date getRemindAt() {
        return mRemindAt;
    }

    public void setRemindAt(Date remindAt) {
        mRemindAt = remindAt;
    }

    public boolean isFinished() {
        return mFinished;
    }

    public void setFinished(boolean finished) {
        mFinished = finished;
    }

    public static Note fromContentValues(ContentValues values) {
        final Note note = new Note();
        if (values.containsKey(COLUMN_ID)) {
            note.setId(values.getAsLong(COLUMN_ID));
        }
        if (values.containsKey(COLUMN_TITLE)) {
            note.setTitle(values.getAsString(COLUMN_TITLE));
        }
        if (values.containsKey(COLUMN_DESCRIPTION)) {
            note.setDescription(values.getAsString(COLUMN_DESCRIPTION));
        }
        if (values.containsKey(COLUMN_CREATED_AT)) {
            note.setCreatedAt(new Date(values.getAsLong(COLUMN_CREATED_AT)));
        }
        if (values.containsKey(COLUMN_REMIND_AT)) {
            note.setRemindAt(new Date(values.getAsLong(COLUMN_REMIND_AT)));
        }
        if (values.containsKey(COLUMN_FINISHED)) {
            note.setFinished(values.getAsBoolean(COLUMN_FINISHED));
        }
        return note;
    }

    public static ContentValues toContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, note.getId());
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DESCRIPTION, note.getDescription());
        values.put(COLUMN_REMIND_AT, note.getRemindAt().getTime());
        values.put(COLUMN_CREATED_AT, note.getCreatedAt().getTime());
        values.put(COLUMN_FINISHED, note.isFinished());
        return values;
    }
}
