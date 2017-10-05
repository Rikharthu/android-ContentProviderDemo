package com.example.rikharthu.contentproviderdemo.utils;

import android.database.Cursor;

import com.example.rikharthu.contentproviderdemo.data.models.Note;

import java.util.Date;

public class Utils {
    public static Note extractNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Note.COLUMN_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Note.COLUMN_TITLE)));
        note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Note.COLUMN_DESCRIPTION)));
        note.setCreatedAt(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Note.COLUMN_CREATED_AT))));
        note.setRemindAt(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(Note.COLUMN_REMIND_AT))));
        note.setFinished(1 == cursor.getInt(cursor.getColumnIndexOrThrow(Note.COLUMN_FINISHED)));
        return note;
    }
}
