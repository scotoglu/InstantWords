package com.scoto.instantwords.ui;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.scoto.instantwords.R;
import com.scoto.instantwords.databinding.FragmentSettingBinding;
import com.scoto.instantwords.utils.SharedPrefHelper;
import com.scoto.instantwords.viewmodel.WordViewModel;

import java.text.DateFormat;
import java.util.Calendar;


public class SettingFragment extends Fragment implements
        TimePickerDialog.OnTimeSetListener,
        AdapterView.OnItemSelectedListener {
    private static final String TAG = "SettingFragment";

    private FragmentSettingBinding binding;
    private SharedPrefHelper sharedPrefManager;

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

        createSharedPref();
        setSpinnerFreqOfNotifyConfig();
        setSpinnerPerDayConfiguration();
        setToolbarConfiguration();
        setTimeTxt();
        setPerDayCount();
        setFreqOfNotify();

        binding.setTime.setOnClickListener(v -> {
            showTimePicker();
        });
        binding.clearAll.setOnClickListener(v -> {
            clearAllData();
        });
        binding.cancelSwitch.setOnClickListener(v -> {
            if (binding.cancelSwitch.isChecked()) {
                Toast.makeText(getContext(), "All Notification is Canceled.", Toast.LENGTH_SHORT).show();
                binding.alarmStartLinear.setVisibility(View.INVISIBLE);
                binding.frequencyLinear.setVisibility(View.INVISIBLE);
                binding.perDayCountLinear.setVisibility(View.INVISIBLE);
            } else {
                binding.alarmStartLinear.setVisibility(View.VISIBLE);
                binding.frequencyLinear.setVisibility(View.VISIBLE);
                binding.perDayCountLinear.setVisibility(View.VISIBLE);
            }
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

    private void showTimePicker() {
        TimePickerFragment pickerFragment = new TimePickerFragment(this::onTimeSet);
        pickerFragment.show(getActivity().getSupportFragmentManager(), "TIME");
    }

    private void createSharedPref() {
        sharedPrefManager = new SharedPrefHelper(getActivity());
    }

    private void setSpinnerFreqOfNotifyConfig() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.frequency_of_notification, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFrequency.setAdapter(adapter);
        binding.spinnerFrequency.setOnItemSelectedListener(this);
        binding.spinnerFrequency.setSelection(0);
    }

    private void setSpinnerPerDayConfiguration() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.not_num_per_day, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPerDay.setAdapter(adapter);
        binding.spinnerPerDay.setOnItemSelectedListener(this);
        binding.spinnerPerDay.setSelection(0);
    }

    private void setToolbarConfiguration() {
        Toolbar toolbar = binding.toolbarSetting;
        toolbar.setTitle("Settings");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void setTimeTxt() {
        String hour = String.valueOf(sharedPrefManager.getPrefHourAndMinute(getString(R.string.hour)));
        String minute = String.valueOf(sharedPrefManager.getPrefHourAndMinute(getString(R.string.minute)));
        String time = hour + ":" + minute;
        if (time != null) {
            binding.time.setText(time);
        }
    }

    private void setPerDayCount() {
        String perDayCount = sharedPrefManager.getPref(getString(R.string.per_day_count));
        if (perDayCount != null) {
            binding.perDayCount.setText(perDayCount);
        }
    }

    private void setFreqOfNotify() {
        String frequency = sharedPrefManager.getPref(getString(R.string.frequency_of_notify));
        if (frequency != null) {
            binding.frequency.setText(frequency);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);


        String timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        binding.time.setText(timeFormat);
        sharedPrefManager.setPrefHourAndMinute(getString(R.string.hour), hourOfDay);
        sharedPrefManager.setPrefHourAndMinute(getString(R.string.minute), minute);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinnerId = parent.getId();
        switch (spinnerId) {
            case R.id.spinnerPerDay:
                if (position > 0) {
                    String value = parent.getItemAtPosition(position).toString();
                    binding.perDayCount.setText(value);
                    sharedPrefManager.setPref(getString(R.string.per_day_count), value);
                } else {
                    //do nothing
                }
                break;
            case R.id.spinnerFrequency:
                if (position > 0) {
                    String value = parent.getItemAtPosition(position).toString();
                    binding.frequency.setText(value);
                    sharedPrefManager.setPref(getString(R.string.frequency_of_notify), value);
                } else {
                    //do nothing
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}