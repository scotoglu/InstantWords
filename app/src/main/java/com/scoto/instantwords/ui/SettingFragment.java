package com.scoto.instantwords.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.scoto.instantwords.R;
import com.scoto.instantwords.databinding.FragmentSettingBinding;
import com.scoto.instantwords.viewmodel.WordViewModel;


public class SettingFragment extends Fragment{
    private static final String TAG = "SettingFragment";

    private FragmentSettingBinding binding;

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
        setToolbarConfiguration();

        binding.clearAll.setOnClickListener(v -> {
            clearAllData();
        });
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
     }