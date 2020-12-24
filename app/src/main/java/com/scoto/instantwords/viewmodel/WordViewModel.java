package com.scoto.instantwords.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.scoto.instantwords.data.model.Word;
import com.scoto.instantwords.data.repository.WordRepository;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private WordRepository repository;
    private LiveData<List<Word>> data;
    private List<Word> wordList;

    public WordViewModel(@NonNull Application application) {
        super(application);
        repository = new WordRepository(application);
        data = repository.getData();
        wordList = repository.getWordList();
    }

    public void insert(Word word) {
        repository.insert(word);
    }

    public void update(Word word) {
        repository.update(word);
    }

    public void delete(Word word) {
        repository.delete(word);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<Word> getAllIsReminded() {
        return repository.getAllIsReminded();
    }

    public LiveData<List<Word>> getData() {
        return data;
    }

    public List<Word> getWordList() {
        return wordList;
    }

    public LiveData<List<Word>> getWordsByCategories(String category) {
        return repository.getWordsByCategory(category);
    }

    public List<Word> getWordsByCategoryList(String category) {
        return repository.getWordsByCategoryList(category);
    }

    public void updateCategoryTitle(String editedCategoryTitle, int wId) {
        repository.updateCategoryTitle(editedCategoryTitle, wId);
    }
}
