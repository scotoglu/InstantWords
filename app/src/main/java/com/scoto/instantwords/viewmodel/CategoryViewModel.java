package com.scoto.instantwords.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.scoto.instantwords.data.model.Category;
import com.scoto.instantwords.data.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository repository;
    private LiveData<List<Category>> categories;
    private List<Category> categoryList;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoryRepository(application);
        categories = repository.getCategories();
    }

    public void delete(Category category) {
        repository.delete(category);
    }

    public void insert(Category category) {
        repository.insert(category);
    }

    public void update(Category category) {
        repository.update(category);
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public List<Category> getCategoryList() {
        return repository.getCategoryList();
    }
}
