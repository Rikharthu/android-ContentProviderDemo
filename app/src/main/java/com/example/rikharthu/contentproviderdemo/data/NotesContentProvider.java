package com.example.rikharthu.contentproviderdemo.data;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.rikharthu.contentproviderdemo.App;
import com.example.rikharthu.contentproviderdemo.data.dao.NoteDao;
import com.example.rikharthu.contentproviderdemo.data.database.AppDatabase;
import com.example.rikharthu.contentproviderdemo.data.models.Note;

import java.util.ArrayList;

import timber.log.Timber;

public class NotesContentProvider extends ContentProvider {

    /**
     * The authority of this content provider
     */
    public static final String AUTHORITY = "com.example.rikharthu.contentproviderdemo.provider";

    /**
     * The URI for the Notes table
     */
    public static final Uri URI_NOTES = Uri.parse("content://" + AUTHORITY + "/" + Note.TABLE_NAME);
    /**
     * The match code for *some* items in the Notes table
     */
    public static final int CODE_NOTE_DIR = 1;
    /**
     * The match code for *an* item in the Notes table
     */
    public static final int CODE_NOTE_ITEM = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // Register URI in our URI matcher
        MATCHER.addURI(AUTHORITY, Note.TABLE_NAME, CODE_NOTE_DIR);
        MATCHER.addURI(AUTHORITY, Note.TABLE_NAME + "/*", CODE_NOTE_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Timber.d("Received query with uri: " + uri.toString());
        final int code = MATCHER.match(uri);
        if (code == CODE_NOTE_DIR || code == CODE_NOTE_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            NoteDao noteDao = App.get(context).getAppDatabase().noteDao();
            final Cursor cursor;
            if (code == CODE_NOTE_DIR) {
                cursor = noteDao.selectAll(); // TODO make cursor version too
            } else {
                cursor = noteDao.selectById(ContentUris.parseId(uri));
            }

            //TODO comments
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            // Will allow CursorLoader to observe changes for this uri
            // from calls getContext().getContentResolver().notifyChange(uri, null);
            // at update(), query() and delete()

            return cursor;
        } else {
            Timber.d("Unknown URI: " + uri);
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    // TODO comments
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_NOTE_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Note.TABLE_NAME;
            case CODE_NOTE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Note.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (MATCHER.match(uri)) {
            case CODE_NOTE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = App.get(context).getAppDatabase().noteDao()
                        .insert(Note.fromContentValues(contentValues));
                // TODO some words about why we notify about change
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_NOTE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        switch (MATCHER.match(uri)) {
            case CODE_NOTE_DIR:
                // Forbid deleting multiple item from content provider
                throw new IllegalArgumentException("Invalid URI, cannot delete without ID" + uri);
            case CODE_NOTE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = App.get(context).getAppDatabase().noteDao()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Timber.d("update: " + uri);
        switch (MATCHER.match(uri)) {
            case CODE_NOTE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_NOTE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Note note = Note.fromContentValues(contentValues);
                note.setId(ContentUris.parseId(uri));
                final int count = App.get(context)
                        .getAppDatabase()
                        .noteDao()
                        .update(note);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        // TODO comments about what's happening here
        final AppDatabase database = App.get(context).getAppDatabase();
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        switch (MATCHER.match(uri)) {
            case CODE_NOTE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final AppDatabase database = App.get(context).getAppDatabase();
                final Note[] notes = new Note[values.length];
                for (int i = 0; i < values.length; i++) {
                    notes[i] = Note.fromContentValues(values[i]);
                }
                return database.noteDao().insertAll(notes).length;
            case CODE_NOTE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
