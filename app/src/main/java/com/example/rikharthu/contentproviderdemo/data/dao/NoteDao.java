package com.example.rikharthu.contentproviderdemo.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.example.rikharthu.contentproviderdemo.data.models.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM " + Note.TABLE_NAME)
    List<Note> getAll();

    @Query("SELECT * FROM " + Note.TABLE_NAME + " WHERE " + Note.COLUMN_ID + " IN (:userIds)")
    List<Note> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM " + Note.TABLE_NAME + " WHERE " + Note.COLUMN_TITLE + " LIKE :title LIMIT 1")
    Note findByTitle(String title);

    @Insert
    long[] insertAll(Note... notes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Note note);

    @Delete
    void delete(Note user);

    @Query("DELETE FROM " + Note.TABLE_NAME + " WHERE " + Note.COLUMN_ID + " = :id")
    int deleteById(long id);

    // Cursor version
    @Query("SELECT * FROM " + Note.TABLE_NAME)
    Cursor selectAll();

    @Query("SELECT * FROM " + Note.TABLE_NAME + " WHERE " + Note.COLUMN_ID + " = :id")
    Cursor selectById(long id);
}
