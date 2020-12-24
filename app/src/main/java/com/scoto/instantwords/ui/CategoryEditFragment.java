package com.scoto.instantwords.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scoto.instantwords.data.model.Category;
import com.scoto.instantwords.data.model.Word;
import com.scoto.instantwords.databinding.FragmentCategoryEditBinding;
import com.scoto.instantwords.ui.adapter.CategoryAdapter;
import com.scoto.instantwords.utils.interfaces.IDialogDismiss;
import com.scoto.instantwords.utils.interfaces.IUpdateCategory;
import com.scoto.instantwords.viewmodel.CategoryViewModel;
import com.scoto.instantwords.viewmodel.WordViewModel;

import java.util.List;

public class CategoryEditFragment extends DialogFragment implements IDialogDismiss, IUpdateCategory {

    private WordViewModel wordViewModel;
    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;
    private FragmentCategoryEditBinding binding;


    public CategoryEditFragment() {
        //Empty Constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setCategoryList(categories);
            categoryAdapter.setContext(getContext());
            categoryAdapter.notifyDataSetChanged();
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordViewModel = new ViewModelProvider(getActivity()).get(WordViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryEditBinding.inflate(getLayoutInflater());
        setRecyclerView();

        return binding.getRoot();
    }

    private void setRecyclerView() {
        categoryAdapter = new CategoryAdapter(this, this, this);
        binding.categoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.categoryRecycler.setHasFixedSize(true);
        binding.categoryRecycler.scrollToPosition(0);
        binding.categoryRecycler.setAdapter(categoryAdapter);
    }

    @Override
    public void onDismiss() {
        dismiss();
    }

    @Override
    public void onUpdateCategory(String newTitle, String oldTitle) {
        //Updates All Fields

        List<Word> wordList = wordViewModel.getWordsByCategoryList(oldTitle);
        if (wordList != null) {
            for (Word word : wordList
            ) {
                wordViewModel.updateCategoryTitle(newTitle, word.getId());
            }
        }


    }
}
