package com.scoto.instantwords.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scoto.instantwords.MainActivity;
import com.scoto.instantwords.R;
import com.scoto.instantwords.data.model.Category;
import com.scoto.instantwords.databinding.FragmentContentBinding;
import com.scoto.instantwords.ui.adapter.WordAdapter;
import com.scoto.instantwords.viewmodel.CategoryViewModel;
import com.scoto.instantwords.viewmodel.ToolbarViewModel;
import com.scoto.instantwords.viewmodel.WordViewModel;

public class ContentFragment extends Fragment {

    private WordViewModel wordViewModel;
    private FragmentContentBinding binding;
    private WordAdapter wordAdapter;
    private SearchView searchView;
    private ToolbarViewModel toolbarViewModel;
    private String selectedCategory = "All";
    private int selectedCategoryId = -1;
    private int DEFAULT_SELECTION = -1;


    public ContentFragment() {
        //Empty Constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        toolbarViewModel = new ViewModelProvider(getActivity()).get(ToolbarViewModel.class);
        DEFAULT_SELECTION = categoryViewModel.getCategoryList().get(0).getId();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        if (selectedCategoryId == DEFAULT_SELECTION) {
            wordViewModel.getData().observe(getViewLifecycleOwner(), words -> {
                wordAdapter.setWordList(words);
                wordAdapter.setContext(getContext());
                wordAdapter.notifyDataSetChanged();
            });
        } else {
            wordViewModel.getWordsByCategories(selectedCategoryId).observe(getViewLifecycleOwner(), words -> {
                wordAdapter.setWordList(words);
                wordAdapter.setContext(getContext());
                wordAdapter.notifyDataSetChanged();
            });
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentContentBinding.inflate(inflater, container, false);
        searchView = getActivity().findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                wordAdapter.getFilter().filter(newText);
                return true;
            }
        });

        Spinner spinner = getActivity().findViewById(R.id.contentFilter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Category category = (Category) parent.getSelectedItem();
                selectedCategoryId = category.getId();
                onActivityCreated(getArguments());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.addBtn.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                    .add(R.id.main_content, new AddFragment())
                    .addToBackStack(null)
                    .commit();
            toolbarViewModel.setToolbarState(false);
        });

        setRecyclerView();
        return binding.getRoot();
    }


    private void setRecyclerView() {
        wordAdapter = new WordAdapter(this);
        binding.recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.scrollToPosition(0);
        binding.recyclerView.setAdapter(wordAdapter);

    }

//    @Override
//    public void onItemSelected(int item) {
//        binding.itemCount.setText(String.valueOf(item));
//        if (item > 0) {
//            binding.appBar.setVisibility(View.VISIBLE);
//            selectedToolbar.setToolbar();
//        }
//    }
//
//    @Override
//    public void onItemUnselected(int item) {
//        binding.itemCount.setText(String.valueOf(item));
//        if (item <= 0) {
//            binding.appBar.setVisibility(View.INVISIBLE);
//            selectedToolbar.resetToolbar();
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();

        toolbarViewModel.setToolbarState(true);
        binding.recyclerView.setClickable(true);
    }

    @Override
    public void onStop() {
        super.onStop();

        toolbarViewModel.setToolbarState(false);
        binding.recyclerView.setClickable(false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null)
            binding = null;
    }
}