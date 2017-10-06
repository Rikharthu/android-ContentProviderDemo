package com.example.rikharthu.contentproviderdemo;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.ContentResolver;
import android.content.Context;

import com.example.rikharthu.contentproviderdemo.data.NotesContentProvider;
import com.example.rikharthu.contentproviderdemo.data.database.AppDatabase;
import com.example.rikharthu.contentproviderdemo.data.models.Note;

import timber.log.Timber;

public class App extends Application {

    public static final String DATABASE_NAME = "notes-database";
    private AppDatabase mAppDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        initializeDatabase();
        seedDatabase();
    }

    private void initializeDatabase() {
        mAppDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME)
                /*.allowMainThreadQueries()*/ // temporary hack to allow dao queries on main thread. do not do in prod
                .build();
    }

    private void seedDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mAppDatabase.noteDao().getAll().isEmpty()) {
                    mAppDatabase.noteDao().insertAll(
                            new Note("Conference call", "Participate in a conference call at 1 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 2 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 3 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 4 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 5 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 6 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 7 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 8 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 9 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 10 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 11 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 12 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 13 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 14 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 15 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 16 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 17 pm", null, null, false),
                            new Note("Conference call", "Participate in a conference call at 18 pm", null, null, false)
                    );
                }
            }
        }).start();
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }
}
