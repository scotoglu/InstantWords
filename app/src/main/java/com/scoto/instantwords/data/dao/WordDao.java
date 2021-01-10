package com.scoto.instantwords.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.scoto.instantwords.data.model.Word;

import java.util.List;

@Dao
public interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Word word);

    @Delete
    void delete(Word word);

    @Update
    void update(Word word);

    @Query("DELETE FROM table_words")
    void deleteAll();

    @Query("SELECT * FROM table_words ORDER BY created_at DESC")
    LiveData<List<Word>> getAll();

    @Query("SELECT * FROM table_words ORDER BY created_at DESC")
    List<Word> getAllWords();

    @Query("SELECT * FROM table_words WHERE is_reminded")
    List<Word> getAllIsReminded();

    @Query("SELECT * FROM table_words WHERE c_id=:c_id ORDER BY created_at DESC")
    LiveData<List<Word>> getWordsByCategory(int c_id);

    @Query("SELECT * FROM table_words WHERE c_id =:c_id ORDER BY created_at DESC")
    List<Word> getWordsByCategoryList(int c_id);

//
//    @Query("UPDATE table_words SET category =:editedCategory WHERE w_id=:wID")
//    void updateCategoryTitle(String editedCategory, int wID);


}
