package com.example.rikharthu.contentproviderdemo.data.converters;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverter;

import com.example.rikharthu.contentproviderdemo.data.models.Note;

import java.util.Date;

public class AppDbConverters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
