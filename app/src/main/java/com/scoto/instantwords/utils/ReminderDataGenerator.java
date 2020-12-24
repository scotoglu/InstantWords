package com.scoto.instantwords.utils;

import android.util.Log;

import com.scoto.instantwords.data.model.Word;

import java.util.List;
import java.util.Random;

public class ReminderDataGenerator {
    private static final String TAG = "ReminderDataGenerator";
    private List<Word> wordList;
    private Word dailyWord;

    public ReminderDataGenerator(List<Word> reminderWords) {
        Log.d(TAG, "ReminderDataGenerator: Constructor");
        this.wordList = reminderWords;
        getRandomWord();
    }

    private void getRandomWord() {

        Random rand = new Random();
        int randNum = rand.nextInt(wordList.size() - 1);

        if (wordList.get(randNum).getIsReminded() == 1) {
            dailyWord = wordList.get(randNum);
        }
    }

    public Word getDailyWord() {
        if (dailyWord != null)
            return dailyWord;
        else
            return null;

    }
}
