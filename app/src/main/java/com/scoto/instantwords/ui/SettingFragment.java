package com.scoto.instantwords.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.scoto.instantwords.R;
import com.scoto.instantwords.databinding.FragmentSettingBinding;
import com.scoto.instantwords.utils.SharedPrefManager;
import com.scoto.instantwords.viewmodel.WordViewModel;


public class SettingFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "SettingFragment";

    private FragmentSettingBinding binding;
    SharedPrefManager sharedPrefManager;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity());
        setToolbarConfiguration();

        binding.clearAll.setOnClickListener(v -> {
            clearAllData();
        });

        setColorSpinner();

        return binding.getRoot();
    }


    private void clearAllData() {
        WordViewModel wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete All");
        builder.setMessage("Are you sure about that?Think again!!!");
        builder.setPositiveButton("Delete Them", (dialog, which) -> {
            wordViewModel.deleteAll();
        });
        builder.setNegativeButton("No, No!", ((dialog, which) -> {
            dialog.dismiss();
        }));
        builder.show();
    }


    private void setToolbarConfiguration() {
        Toolbar toolbar = binding.toolbarSetting;
        toolbar.setTitle("Settings");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void setColorSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.colors));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.defaultColor.setAdapter(adapter);
        binding.defaultColor.setOnItemSelectedListener(this);
        getDefaultColor();
    }

    private void getDefaultColor() {
        String defaultColor = sharedPrefManager.getDefaultColor();
        if (defaultColor != null) {
            binding.selectedDefaultColor.setText(defaultColor.toUpperCase());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: " + parent.getItemAtPosition(position).toString());
        //Save, default color selection to
        if (position > 0) {//Avoid to select 'Select' option
            sharedPrefManager.setDefaultColor(parent.getItemAtPosition(position).toString().toLowerCase());
        } else {
            //do nothing
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}