package com.scoto.instantwords.utils;

import android.util.Log;
import android.widget.Filter;

import com.scoto.instantwords.data.model.Word;
import com.scoto.instantwords.ui.adapter.WordAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SearchFilter extends Filter {
    private static final String TAG = "SearchFilter";
    private List<Word> filteredList;
    private List<Word> originalList;
    private WordAdapter wordAdapter;

    public SearchFilter(WordAdapter adapter, List<Word> wordList) {
        this.originalList = wordList;
        this.wordAdapter = adapter;
        Log.d(TAG, "SearchFilter: Size: " + wordList.size());
        this.filteredList = new ArrayList<>();
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();
        if (constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();
            for (Word w : originalList) {
                if (w.getWord().toLowerCase().contains(filterPattern)) {
                    filteredList.add(w);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        wordAdapter.getWordList().clear();
        wordAdapter.getWordList().addAll((Collection<? extends Word>) results.values);
        wordAdapter.notifyDataSetChanged();
        Log.d(TAG, "publishResults: Size: " + originalList.size());
    }
}
