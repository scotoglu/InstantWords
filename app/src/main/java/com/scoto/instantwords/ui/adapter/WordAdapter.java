package com.scoto.instantwords.ui.adapter;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.scoto.instantwords.MainActivity;
import com.scoto.instantwords.R;
import com.scoto.instantwords.data.model.Word;
import com.scoto.instantwords.databinding.WordItemBinding;
import com.scoto.instantwords.ui.WordFragment;
import com.scoto.instantwords.utils.SearchFilter;
import com.scoto.instantwords.viewmodel.WordViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "WordAdapter";
    private List<Word> wordList, selectedList;
    private SelectedCount selectedCount;
    private Context context;
    private WordViewModel wordViewModel;
    private HashMap<Word, Integer> selected;
    private SearchFilter filter;
    private List<Word> filteredList;



    public WordAdapter(SelectedCount count, Fragment fragment) {
        wordViewModel = new ViewModelProvider(fragment).get(WordViewModel.class);
        this.selectedCount = count;
        this.selectedList = new ArrayList<>();
        this.selected = new HashMap<>();
        this.filteredList = new ArrayList<>();
    }

    public interface SelectedCount {
        void onSelectedItem(int data);

        void onUnSelectedItem(int data);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        WordItemBinding binding = WordItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Word word = wordList.get(position);
        holder.bind(word);
        holder.wordItemBinding.cardView.setOnClickListener(v -> {
//            if (!word.isSelected) {
//                word.setSelected(true);
//                selected.put(word, position);
//                setSelected(holder);
//                selectedCount.onSelectedItem(selected.size());
//            } else {
//                word.setSelected(false);
//                selected.remove(word);
//                unselected(holder, word);
//                selectedCount.onUnSelectedItem(selected.size());
//            }

            //TODO handle first deletion.
            //TODO activate with onClick.
            WordFragment wordFragment = WordFragment.newInstance(word);
            FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_content, wordFragment).addToBackStack(null).commit();

        });
    }

//    private void setSelected(ViewHolder holder) {
//        holder.wordItemBinding.cardLinear.setBackgroundColor(context.getColor(R.color.appbar_bg));
//    }
//
//    private void unselected(ViewHolder holder, Word word) {
//        holder.wordItemBinding.cardLinear.setBackgroundColor(Color.parseColor(word.getBgColor()));
//    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filteredList.clear();
            filteredList.addAll(getWordList());
            filter = new SearchFilter(this, filteredList);
        }
        return filter;
    }

    public List<Word> getWordList() {
        return wordList;
    }

    @Override
    public int getItemCount() {
        return wordList != null ? wordList.size() : 0;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void setWordList(List<Word> words) {
        this.wordList = words;
        notifyDataSetChanged();
    }

    public void resetState() {
        if (selected.size() > 0 ) {
            selected.clear();
        }
        notifyDataSetChanged();
    }

    public void delete() {
        if (selected.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                selected.forEach((word, position) -> {
                    removeItem(position, word);
                });
            }
        }
        selected.clear();
    }

    private void removeItem(int pos, Word word) {
        wordViewModel.delete(word);
        notifyItemRemoved(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private WordItemBinding wordItemBinding;

        public ViewHolder(WordItemBinding binding) {
            super(binding.getRoot());
            this.wordItemBinding = binding;
        }

        public void bind(Word word) {
            wordItemBinding.setWord(word);
            wordItemBinding.executePendingBindings();
        }

    }
}
