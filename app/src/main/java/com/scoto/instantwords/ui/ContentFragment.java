package com.scoto.instantwords.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scoto.instantwords.R;
import com.scoto.instantwords.data.model.Word;
import com.scoto.instantwords.databinding.FragmentContentBinding;
import com.scoto.instantwords.ui.adapter.WordAdapter;
import com.scoto.instantwords.viewmodel.ItemViewModel;
import com.scoto.instantwords.viewmodel.WordViewModel;

import java.util.List;

public class ContentFragment extends Fragment implements WordAdapter.SelectedCount {


    private static final String TAG = "ContentFragment";
    private WordViewModel wordViewModel;
    private FragmentContentBinding binding;
    private WordAdapter wordAdapter;
    private CoordinatorLayout.LayoutParams layoutParams;
    private SelectedToolbar selectedToolbar;
    private SearchView searchView;
    private ItemViewModel itemViewModel;
    private String selectedCategory = "All";


    //Interface to control MainActivity Toolbar reaction.
    public interface SelectedToolbar {
        void setToolbar();

        void resetToolbar();
    }


    public ContentFragment(SelectedToolbar selectedToolbar) {
        this.selectedToolbar = selectedToolbar;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemViewModel = new ViewModelProvider(getActivity()).get(ItemViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        if (selectedCategory.equals("All")) {
            wordViewModel.getData().observe(getViewLifecycleOwner(), words -> {
                wordAdapter.setWordList(words);
                wordAdapter.setContext(getContext());
                wordAdapter.notifyDataSetChanged();
            });
        } else {
            wordViewModel.getWordsByCategories(selectedCategory).observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                @Override
                public void onChanged(List<Word> words) {
                    wordAdapter.setWordList(words);
                    wordAdapter.setContext(getContext());
                    wordAdapter.notifyDataSetChanged();
                }
            });
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        layoutParams = (CoordinatorLayout.LayoutParams) binding.coordinatorLayout.getLayoutParams();
        binding.cancel.setOnClickListener(v -> {
            binding.appBar.setVisibility(View.INVISIBLE);
            itemViewModel.setData(true);
            wordAdapter.resetState();
        });

        Spinner spinner = getActivity().findViewById(R.id.contentFilter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                onActivityCreated(getArguments());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemViewModel.setData(true);

        binding.delete.setOnClickListener(v -> {
            wordAdapter.delete();
            binding.appBar.setVisibility(View.INVISIBLE);
            selectedToolbar.resetToolbar();
        });

        binding.addBtn.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                    .add(R.id.main_content, new AddFragment())
                    .addToBackStack(null)
                    .commit();
            selectedToolbar.setToolbar();
        });

        setRecyclerView();

        return binding.getRoot();
    }

    private void setRecyclerView() {
        wordAdapter = new WordAdapter(this, this);
        binding.recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.scrollToPosition(0);
        binding.recyclerView.setAdapter(wordAdapter);

    }

    @Override
    public void onSelectedItem(int count) {
        binding.itemCount.setText(String.valueOf(count));
        if (count > 0) {
            Log.d(TAG, "onSelectedItem: IF");
            binding.appBar.setVisibility(View.VISIBLE);
            selectedToolbar.setToolbar();
        }
    }

    @Override
    public void onUnSelectedItem(int count) {
        binding.itemCount.setText(String.valueOf(count));
        if (count <= 0) {
            binding.appBar.setVisibility(View.INVISIBLE);
            selectedToolbar.resetToolbar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        selectedToolbar.resetToolbar();
        binding.recyclerView.setClickable(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        selectedToolbar.setToolbar();
        binding.recyclerView.setClickable(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null)
            binding = null;
    }
}