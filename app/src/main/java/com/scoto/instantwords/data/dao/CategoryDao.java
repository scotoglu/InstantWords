package com.scoto.instantwords.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.scoto.instantwords.data.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {


    @Update
    void updateCategory(Category category);

    @Insert
    void insertCategory(Category categories);

    @Delete
    void deleteCategory(Category category);

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM categories")
    List<Category> getAllCategoriesAsList();

}
