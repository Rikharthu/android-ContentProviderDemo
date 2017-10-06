package com.example.rikharthu.contentproviderdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.rikharthu.contentproviderdemo.data.NotesContentProvider;
import com.example.rikharthu.contentproviderdemo.ui.adapters.NotesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final int LOADER_NOTES = 1;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private NotesAdapter mNotesAdapter;
    private LoaderManager.LoaderCallbacks<? extends Object> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        // Called when the system needs a new loader to be created
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // Create a new loader based on the passed id
            Timber.d("onCreateLoader");
            switch (id) {
                case LOADER_NOTES:
                    Timber.d("Creating Notes Loader");
                    // Our notes loader will fetch data from the content provider, thus it needs to be a CursorLoader
                    // These parameters will be used on ContentProvider's query() method
                    return new CursorLoader(getApplicationContext(),
                            NotesContentProvider.URI_NOTES,
                            null,
                            null,
                            null,
                            null);
                // Also inside NoteContentProvider#query() we register a notification URI
                // cursor.setNotificationUri(getContext().getContentResolver(), uri);
                // which will be used by this CursorLoader to watch for changes and trigger it's loading
                default:
                    throw new IllegalArgumentException();
            }
        }

        // Called when a loader has finished loading data.
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // Display the data to the user
            switch (loader.getId()) {
                case LOADER_NOTES:
                    Timber.d("Notes Loader finished loading data");
                    mNotesAdapter.setNotes(data);
                    break;
            }
        }

        // Previously created loader has been reset (destroyLoader(int)/activity or fragment is destroyed)
        // Data is no longer available, removy any references to it
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Timber.d("onLoaderReset");
            switch (loader.getId()) {
                case LOADER_NOTES:
                    Timber.d("Notes Loader has been reset");
                    mNotesAdapter.setNotes(null);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecycler.setLayoutManager(new LinearLayoutManager(mRecycler.getContext()));
        mNotesAdapter = new NotesAdapter();
        mRecycler.setAdapter(mNotesAdapter);

        /* Loaders run on a separate threads
        * Loaders provide callback methods when events occur
        * Loaders persist and cache results across configuration changes to prevent duplicate queries
        * Loaders can implement an observer to monitor changes in the underlying data source
        *   CursorLoader automatically registers a ContentObserver to trigger reload when data changes
        *   see NotesContentProvider#query() : cursor.setNotificationUri(getContext().getContentResolver(), uri);
        *
        * More info: https://developer.android.com/guide/components/loaders.html
        */
        // Register a new loader for notes
        getSupportLoaderManager() // class associated with single activity/fragment to manage one or more loaders
                .initLoader( // start loading data from a loader
                        LOADER_NOTES,       // Loader ID (if it already exists, then it's reused and args are not passed)
                        null,          // optional args passed to LoaderCallbacks#onCreateLoader()
                        mLoaderCallbacks);  // callbacks for loader events

        // TODO for debug
        addNewWord("Tomato", 100);
        getUserDictionary();
    }

    private void addNewWord(String word, int frequency) {
        Uri newUri;
        ContentValues wordValues = new ContentValues();
        wordValues.put(UserDictionary.Words.APP_ID, "com.example.rikharthu.contentproviderdemo");
        wordValues.put(UserDictionary.Words.LOCALE, "en_US");
        wordValues.put(UserDictionary.Words.WORD, word);
        wordValues.put(UserDictionary.Words.FREQUENCY, frequency);
        newUri = getContentResolver().insert(
                UserDictionary.Words.CONTENT_URI,
                wordValues
        );
        Timber.d("Inserted word " + word + " (" + frequency + ") at uri " + newUri);
    }

    private void getUserDictionary() {
        /*
        Cursor dictionaryCursor = getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,       // The content URI of the words table
                new String[]{UserDictionary.Words._ID,  // Projection, which columns to select
                        UserDictionary.Words.WORD,
                        UserDictionary.Words.LOCALE},
                UserDictionary.Words.WORD + " = ?",
                new String[]{"Tomato"},                 // will result into "word = Tomato"
                null
        );
        */
        Cursor dictionaryCursor = getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        int count = dictionaryCursor.getCount();
        int a = 4;
        dictionaryCursor.close();
    }
}
