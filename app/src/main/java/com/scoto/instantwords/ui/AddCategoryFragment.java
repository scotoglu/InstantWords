package com.scoto.instantwords.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.scoto.instantwords.MainActivity;
import com.scoto.instantwords.data.model.Category;
import com.scoto.instantwords.databinding.FragmentAddCategoryBinding;
import com.scoto.instantwords.viewmodel.CategoryViewModel;

public class AddCategoryFragment extends DialogFragment {

    private FragmentAddCategoryBinding binding;
    private CategoryViewModel categoryViewModel;


    public AddCategoryFragment(MainActivity mainActivity) {
        //Empty Constructor

        categoryViewModel = new ViewModelProvider(mainActivity).get(CategoryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnAddCategory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Category added: " + binding.addCategory.getText(), Toast.LENGTH_SHORT).show();
            String newCategory = binding.addCategory.getText().toString().trim();
            addNewCategory(newCategory);
            Toast.makeText(getActivity(), newCategory + " is added.", Toast.LENGTH_SHORT).show();
            dismiss();
        });


        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void addNewCategory(String newCategory) {
        categoryViewModel.insert(new Category(newCategory));
    }
}
