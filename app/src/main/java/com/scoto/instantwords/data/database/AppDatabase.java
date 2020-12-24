package com.scoto.instantwords.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.scoto.instantwords.data.dao.CategoryDao;
import com.scoto.instantwords.data.dao.WordDao;
import com.scoto.instantwords.data.model.Category;
import com.scoto.instantwords.data.model.Word;

@Database(entities = {Word.class, Category.class}, version = 2,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static String DBNAME = "instant_word";
    private static AppDatabase INSTANCE;

    public abstract WordDao wordDao();

    public abstract CategoryDao categoryDao();


    public static AppDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DBNAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
