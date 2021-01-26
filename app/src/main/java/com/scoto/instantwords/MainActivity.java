package com.scoto.instantwords;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.scoto.instantwords.data.model.Category;
import com.scoto.instantwords.databinding.ActivityMainBinding;
import com.scoto.instantwords.ui.AboutFragment;
import com.scoto.instantwords.ui.AddCategoryFragment;
import com.scoto.instantwords.ui.CategoryEditFragment;
import com.scoto.instantwords.ui.ContentFragment;
import com.scoto.instantwords.ui.SettingFragment;
import com.scoto.instantwords.viewmodel.CategoryViewModel;
import com.scoto.instantwords.viewmodel.ToolbarViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    private ToolbarViewModel toolbarViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Controls toolbar visibility when fragment changes.
        toolbarViewModel = new ViewModelProvider(this).get(ToolbarViewModel.class);
        toolbarViewModel.getToolbarState().observe(this, aBoolean -> {
            if (aBoolean) {
                binding.appBarMain.setVisibility(View.VISIBLE);
            } else {
                binding.appBarMain.setVisibility(View.INVISIBLE);
            }
        });
        //Load ContentFragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_content, new ContentFragment()).commit();


        //Set and initialize views´´
        configurationToolbar();
        configurationSearchView();
        configurationDrawerLayout();
        configurationNavigationView();
        configurationSpinner();
    }


    private void configurationSpinner() {
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.contentFilter.setAdapter(adapter);
            }
        });

    }

    private void configurationSearchView() {
        binding.searchView.setOnClickListener(v -> {

                    binding.searchView.setIconified(false);
                    binding.searchView.setQueryHint("Search Words");

                }
        );
    }

    private void configurationToolbar() {
        setSupportActionBar(binding.toolbarMain);
        setTitle("");
    }

    private void configurationNavigationView() {
        binding.navigationView.setNavigationItemSelectedListener(this);
    }

    private void configurationDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbarMain, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));
        toggle.setHomeAsUpIndicator(R.mipmap.ic_hamburger);
        toggle.syncState();
    }


    private void addCategory() {
        //create new category
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new AddCategoryFragment(this);
        dialogFragment.show(ft, "dialog");


    }

    private void editCategory() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("edit");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment fragment = new CategoryEditFragment();
        fragment.show(ft, "edit");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm;
        FragmentTransaction ft;
        switch (item.getItemId()) {
            case R.id.editCategory:
                editCategory();
                break;
            case R.id.addCategory:
                addCategory();
                break;
            case R.id.add:
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                binding.drawerLayout.closeDrawers();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.main_content, new SettingFragment()).addToBackStack(null).commit();
                break;
            case R.id.share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                binding.drawerLayout.closeDrawers();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.main_content, new AboutFragment()).addToBackStack(null).commit();
                break;
            default:
                break;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding = null;
        }
    }

}