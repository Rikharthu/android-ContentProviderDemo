package com.example.rikharthu.contentproviderdemo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.rikharthu.contentproviderdemo.data.NotesContentProvider;
import com.example.rikharthu.contentproviderdemo.data.models.Note;
import com.example.rikharthu.contentproviderdemo.ui.adapters.NotesAdapter;
import com.example.rikharthu.contentproviderdemo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final int LOADER_NOTES = 1;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private NotesAdapter mNotesAdapter;
    private LoaderManager.LoaderCallbacks<? extends Object> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Timber.d("onCreateLoader");
            switch (id) {
                case LOADER_NOTES:
                    return new CursorLoader(getApplicationContext(),
                            NotesContentProvider.URI_NOTES,
                            null,
                            null,
                            null,
                            null);
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Timber.d("Cursor finished loading data");
            switch (loader.getId()) {
                case LOADER_NOTES:
                    mNotesAdapter.setNotes(data);
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Timber.d("onLoaderReset");
            switch (loader.getId()) {
                case LOADER_NOTES:
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

        //TODO for debug
        Note note = new Note();
        note.setId(2);
        note.setDescription("HAHAHAHA");
        note.setTitle("MUAHAHA");
        note.setFinished(true);
        App.get(this).getAppDatabase().noteDao().update(note);

        List<Note> notes = App.get(this).getAppDatabase().noteDao().getAll();
        Cursor c = App.get(this).getAppDatabase().noteDao().selectAll();
        List<Note> cursorNotes = new ArrayList<>();
        while (c.moveToNext()) {
            cursorNotes.add(Utils.extractNote(c));
        }

        mRecycler.setLayoutManager(new LinearLayoutManager(mRecycler.getContext()));
        mNotesAdapter = new NotesAdapter();
        mRecycler.setAdapter(mNotesAdapter);

//        mNotesAdapter.setNotes(getContentResolver().query(
//                NotesContentProvider.URI_NOTES,
//                null,
//                null,
//                null,
//                null));
        getSupportLoaderManager().initLoader(LOADER_NOTES, null, mLoaderCallbacks);
    }
}
