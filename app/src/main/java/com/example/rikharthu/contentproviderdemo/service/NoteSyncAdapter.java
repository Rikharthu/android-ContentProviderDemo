package com.example.rikharthu.contentproviderdemo.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.example.rikharthu.contentproviderdemo.App;
import com.example.rikharthu.contentproviderdemo.data.NotesContentProvider;
import com.example.rikharthu.contentproviderdemo.data.dao.NoteDao;
import com.example.rikharthu.contentproviderdemo.data.models.Note;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class NoteSyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver mContentResolver;
    private AccountManager mAccountManager;
    private NoteDao mNoteDao;

    public NoteSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        init();
    }

    public NoteSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        init();
    }

    private void init() {
        mContentResolver = getContext().getContentResolver();
        mNoteDao = App.get(getContext()).getAppDatabase().noteDao();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Timber.d("Performing sync for account %s", account.name);

        try {
            List<Note> notesFromServer = fetchNotes();

            postToServer();
            for (Note note : notesFromServer) {
                mContentResolver.insert(NotesContentProvider.URI_NOTES, Note.toContentValues(note));
            }


            // P.S. To make things easier, consider creating a database column as follows:
            // _id - Local ID
            // server_id - After syncing, every row will get the server's ID
            // status_flay - CLEAN, MOD, ADD, DELETE

            // And after getting result from server, delete all local notes that are no longer in the server
            // To do it get a list of notes from our local database with the CLEAN flag, and check whether
            // it is in the server's response. If not - delete it

            // Then iterate over server notes and check whether it exists locally.
            // If it exists - we update it with the information from the server
            // If not - we create a new one

            // Last step is to post new changes from local database to the server
            // Get every note from the local database that is not CLEAN (has been modified)
            // and then, depending on flag status, push to the server/update/delete
        } catch (InterruptedException e) {
            Timber.e(e);
            handleSyncFailure(syncResult);
        }
    }

    private void handleSyncFailure(SyncResult syncResult) {
        Timber.d("Handling sync failure");
        // TODO handle sync failure
        syncResult.stats.numIoExceptions++;
    }

    private void postToServer() {
        Timber.d("Posting some data to server");
        // TODO post local changes to server
    }

    private List<Note> fetchNotes() throws InterruptedException {
        // mock a server call and return some notes
        Thread.sleep(5000);

        int counter = 0;
        return Arrays.asList(
                new Note("Note from server #" + (counter++), "Hello from the SyncAdapter!",  new Date(), new Date(), false),
                new Note("Note from server #" + (counter++), "Hello from the SyncAdapter!",  new Date(), new Date(), false),
                new Note("Note from server #" + (counter++), "Hello from the SyncAdapter!",  new Date(), new Date(), false),
                new Note("Note from server #" + (counter++), "Hello from the SyncAdapter!",  new Date(), new Date(), false));
    }
}
