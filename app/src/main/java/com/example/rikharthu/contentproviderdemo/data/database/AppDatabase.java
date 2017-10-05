package com.example.rikharthu.contentproviderdemo.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.rikharthu.contentproviderdemo.data.converters.AppDbConverters;
import com.example.rikharthu.contentproviderdemo.data.dao.NoteDao;
import com.example.rikharthu.contentproviderdemo.data.models.Note;

@Database(entities = Note.class, version = 1)
@TypeConverters({AppDbConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();
}
