package com.scoto.instantwords.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;

import com.scoto.instantwords.data.dao.CategoryDao;
import com.scoto.instantwords.data.dao.WordDao;
import com.scoto.instantwords.data.model.Category;
import com.scoto.instantwords.data.model.Word;

@Database(entities = {Word.class, Category.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static String DBNAME = "instant_word1";
    private static AppDatabase INSTANCE;

    public abstract WordDao wordDao();

    public abstract CategoryDao categoryDao();


    public static AppDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DBNAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            prePopulateCategoryTable(db);
        }
    };


    private static void prePopulateCategoryTable(SupportSQLiteDatabase db) {
        String sql = "INSERT INTO categories(c_id,category_title) VALUES(?,?)";
        SupportSQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(2, "All");
        statement.executeInsert();

    }
}
