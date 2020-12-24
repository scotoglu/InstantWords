package com.scoto.instantwords.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.scoto.instantwords.data.dao.CategoryDao;
import com.scoto.instantwords.data.database.AppDatabase;
import com.scoto.instantwords.data.model.Category;

import java.util.List;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> categories;
    private List<Category> categoryList;

    public CategoryRepository(Application application) {
        categoryDao = AppDatabase.getINSTANCE(application).categoryDao();
        categories = categoryDao.getAllCategories();
    }

    public void update(Category category) {
        categoryDao.updateCategory(category);
    }

    public void insert(Category category) {
        categoryDao.insertCategory(category);
    }

    public void delete(Category category) {
        categoryDao.deleteCategory(category);
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public List<Category> getCategoryList() {
        return categoryDao.getAllCategoriesAsList();
    }
}
