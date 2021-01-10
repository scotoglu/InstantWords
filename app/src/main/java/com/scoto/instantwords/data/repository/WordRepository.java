package com.scoto.instantwords.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.scoto.instantwords.data.dao.WordDao;
import com.scoto.instantwords.data.database.AppDatabase;
import com.scoto.instantwords.data.model.Word;

import java.util.List;

public class WordRepository {
    private WordDao wordDao;
    private LiveData<List<Word>> data;
    private List<Word> wordList;

    public WordRepository(Application application) {

        AppDatabase appDatabase = AppDatabase.getINSTANCE(application);
        wordDao = appDatabase.wordDao();
        data = wordDao.getAll();
        wordList = wordDao.getAllWords();
    }


    public void insert(Word word) {
        wordDao.insert(word);
    }

    public void update(Word word) {
        wordDao.update(word);
    }

    public void delete(Word word) {
        wordDao.delete(word);
    }

    public void deleteAll() {
        wordDao.deleteAll();
    }

    public List<Word> getAllIsReminded() {
        return wordDao.getAllIsReminded();
    }

    public LiveData<List<Word>> getData() {
        return data;
    }

    public List<Word> getWordList() {
        return wordList;
    }

    public LiveData<List<Word>> getWordsByCategory(int  categoryId) {
        return wordDao.getWordsByCategory(categoryId);
    }

    public List<Word> getWordsByCategoryList(int categoryId) {
        return wordDao.getWordsByCategoryList(categoryId);
    }

//    public void updateCategoryTitle(String editedCategoryTitle, int wID) {
//        wordDao.updateCategoryTitle(editedCategoryTitle, wID);
//    }
}
