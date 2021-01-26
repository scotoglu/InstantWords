package com.scoto.instantwords.ui.adapter;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
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
    private List<Word> wordList;

    private Context context;
    private WordViewModel wordViewModel;
    private HashMap<Word, Integer> selected;
    private SearchFilter filter;
    private List<Word> filteredList;
    private Fragment fragment;

    //Variables for ActionMode.
    private boolean isEnable = false;
    private boolean isSelectAll = false;
    private List<Word> selectedList = new ArrayList<>();

    public WordAdapter(Fragment fragment) {
        wordViewModel = new ViewModelProvider(fragment).get(WordViewModel.class);

        this.fragment = fragment;
        this.selectedList = new ArrayList<>();
        this.selected = new HashMap<>();
        this.filteredList = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //    acMoTextViewModel = new ViewModelProvider(fragment).get(AcMoTextViewModel.class);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        WordItemBinding binding = WordItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Word word = wordList.get(position);
        holder.bind(word);
//        holder.wordItemBinding.cardLinear.setOnLongClickListener(v -> {
//            if (!isEnable) {
//                //When action mode is not enable
//                //Initialize action mode
//                ActionMode.Callback callback = new ActionMode.Callback() {
//                    @Override
//                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                        Log.d(TAG, "onCreateActionMode: Called");
//                        //Initializing menu inflater
//                        MenuInflater menuInflater = mode.getMenuInflater();
//                        menuInflater.inflate(R.menu.action_menu, menu);
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                        Log.d(TAG, "onPrepareActionMode: Called");
//                        //When action mode is prepare
//                        isEnable = true;
//                        //called each time when item clicked
//                        clickItem(holder);
//                        //Set observer
//                        acMoTextViewModel.getText().observe(fragment, s -> {
//                            mode.setTitle(String.format("%s Selected", s));
//                        });
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                        Log.d(TAG, "onActionItemClicked: ");
//                        int id = item.getItemId();
//                        switch (id) {
//                            case R.id.deleteMul:
//                                //Delete from db and list. For now just from list.
//                                for (Word w : wordList
//                                ) {
//                                    wordList.remove(w);
//                                }
//                                mode.finish();
//                                break;
//                            case R.id.selectAll:
//                                if (selectedList.size() == wordList.size()) {
//                                    //When all item selected.
//                                    //Set isSelectAll  false;
//                                    isSelectAll = false;
//                                    selectedList.clear();
//                                } else {
//                                    //When all item unselected
//                                    isSelectAll = true;
//                                    selectedList.clear();
//                                    selectedList.addAll(wordList);
//                                }
//                                acMoTextViewModel.setText(String.valueOf(selectedList.size()));
//                                notifyDataSetChanged();
//                                break;
//                        }
//                        return true;
//                    }
//
//                    @Override
//                    public void onDestroyActionMode(ActionMode mode) {
//                        Log.d(TAG, "onDestroyActionMode: ");
//                        //When action mode is destroy
//                        isEnable = false;
//                        isSelectAll = false;
//                        selectedList.clear();
//                        notifyDataSetChanged();
//                    }
//                };
//                ((AppCompatActivity) v.getContext()).startSupportActionMode(callback);
//            } else {
//                //When action mode is already enable
//                clickItem(holder);
//            }
//
//            if (isSelectAll) {
//                //When all item selected
//                holder.wordItemBinding.cardLinear.setBackgroundColor(Color.LTGRAY);
//            } else {
//                //when all item unselected
//                //Hide all checkbox
//                holder.wordItemBinding.cardLinear.setBackgroundColor(Color.parseColor(word.getBgColor()));
//            }
//            return true;
//        });

        holder.wordItemBinding.cardLinear.setOnClickListener(v -> {
            WordFragment wordFragment = WordFragment.newInstance(word);
            FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_content, wordFragment).addToBackStack(null).commit();
        });
    }

//    private void clickItem(ViewHolder holder) {
//
//        Word word = getWordList().get(holder.getAdapterPosition());
//
//        if (!word.isSelected()) {
//            //When item not selected
//            holder.wordItemBinding.cardLinear.setBackgroundColor(Color.parseColor(word.getBgColor()));
//            word.setSelected(true);
//            selectedList.add(word);
//        } else {
//            //when item selected
//            holder.wordItemBinding.cardLinear.setBackgroundColor(Color.LTGRAY);
//            word.setSelected(false);
//            //remove item from selected list
//            selectedList.remove(word);
//        }
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
        if (selected.size() > 0) {
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

            wordItemBinding.cardLinear.setOnLongClickListener(v -> {

                wordItemBinding.checkboxLayout.setVisibility(View.VISIBLE);

                return false;
            });

        }

        public void bind(Word word) {
            wordItemBinding.setWord(word);
            wordItemBinding.executePendingBindings();
        }

    }
}
