package com.scoto.instantwords.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.scoto.instantwords.R;
import com.scoto.instantwords.data.model.Word;
import com.scoto.instantwords.databinding.ActivityNotificationBinding;

public class NotificationActivity extends AppCompatActivity {


    private ActivityNotificationBinding binding;
    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}