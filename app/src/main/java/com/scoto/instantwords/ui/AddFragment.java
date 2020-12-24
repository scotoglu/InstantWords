package com.scoto.instantwords.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.scoto.instantwords.R;
import com.scoto.instantwords.data.model.Category;
import com.scoto.instantwords.data.model.Word;
import com.scoto.instantwords.databinding.FragmentAddBinding;
import com.scoto.instantwords.viewmodel.CategoryViewModel;
import com.scoto.instantwords.viewmodel.ItemViewModel;
import com.scoto.instantwords.viewmodel.WordViewModel;

import java.util.ArrayList;
import java.util.List;


public class AddFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "AddFragment";

    private FragmentAddBinding binding;
    private String colorCode = "#FFFFFFFF";
    private WordViewModel wordViewModel;
    private View view;
    private ItemViewModel itemViewModel;
    private CategoryViewModel categoryViewModel;
    private String selectedCategory;

    public AddFragment() {
        // Required empty public constructor
    }


    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        itemViewModel = new ViewModelProvider(getActivity()).get(ItemViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        setListeners();
        setSpinner();

        binding.addWord.setOnClickListener(v -> addWord());
        return view;
    }

    private void setSpinner() {
        List<Category> categories = categoryViewModel.getCategoryList();
        List<String> list = new ArrayList<>();
        list.add("Select Category");
        for (Category category : categories
        ) {
            list.add(category.getTitle());
        }
        if (list.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.categorySpinner.setAdapter(adapter);
        }

        binding.categorySpinner.setOnItemSelectedListener(this);

    }

    private void setListeners() {
        binding.relativeLayoutCancel.setOnClickListener(this::onClick);
        binding.cancel.setOnClickListener(this::onClick);
        binding.red.setOnClickListener(this::onClick);
        binding.blue.setOnClickListener(this::onClick);
        binding.green.setOnClickListener(this::onClick);
        binding.yellow.setOnClickListener(this::onClick);
        binding.purple.setOnClickListener(this::onClick);
    }


    private void addWord() {

        String word = binding.wordEdt.getText().toString();
        String definition = binding.definitionEdt.getText().toString();

        if (word.isEmpty() || definition.isEmpty()) {
            Toast.makeText(getContext(), "Fill all fields.", Toast.LENGTH_SHORT).show();
        } else {
            if (selectedCategory != null) {
                Word data = new Word(word, definition, selectedCategory);
                data.setBgColor(colorCode);
                wordViewModel.insert(data);
                cancelToAdding();//Close current fragment
            } else {
                selectedCategory = "";
            }

        }


    }

    private void cancelToAdding() {
        getActivity().getSupportFragmentManager().popBackStack();
        itemViewModel.setData(true);
        onDetach();
    }

    @Override
    public void onClick(View v) {
        int itemId = v.getId();
        switch (itemId) {
            case R.id.red:
                binding.formLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_alizarin));
                colorCode = "#E74C3C";
                break;
            case R.id.green:
                binding.formLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_emerald));
                colorCode = "#2ecc71";
                break;
            case R.id.blue:
                binding.formLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_peter_rıver));
                colorCode = "#2980B9";
                break;
            case R.id.yellow:
                binding.formLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_sun_flower));
                colorCode = "#F1C40F";
                break;
            case R.id.purple:
                binding.formLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_amethyst));
                colorCode = "#9b59b6";
                break;
            case R.id.cancel:
            case R.id.relativeLayoutCancel:
                cancelToAdding();
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position > 0) {
            String category = parent.getItemAtPosition(position).toString();
            selectedCategory = category;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //if soft keyboard is active, close keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null)
            binding = null;
    }
}