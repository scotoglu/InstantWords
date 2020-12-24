package com.scoto.instantwords;

import android.os.Bundle;
import android.util.Log;
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
import com.scoto.instantwords.data.model.Word;
import com.scoto.instantwords.databinding.ActivityMainBinding;
import com.scoto.instantwords.ui.AboutFragment;
import com.scoto.instantwords.ui.AddCategoryFragment;
import com.scoto.instantwords.ui.CategoryEditFragment;
import com.scoto.instantwords.ui.ContentFragment;
import com.scoto.instantwords.ui.SettingFragment;
import com.scoto.instantwords.utils.AlarmHelper;
import com.scoto.instantwords.viewmodel.CategoryViewModel;
import com.scoto.instantwords.viewmodel.ItemViewModel;
import com.scoto.instantwords.viewmodel.WordViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ContentFragment.SelectedToolbar {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private CategoryViewModel categoryViewModel;
    private ItemViewModel itemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //TODO use ItemViewModel open/close AppBarLayout in MainActivity, change with Interfaces are SelectedToolbar and SelectedCount
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getData().observe(this, aBoolean -> {
            Log.d(TAG, "onCreate: ");
            if (aBoolean) {
                binding.appBarMain.setVisibility(View.VISIBLE);
            }
        });

        //Load ContentFragment
        ContentFragment contentFragment = new ContentFragment(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.main_content, contentFragment).commit();

        configurationToolbar();
        configurationSearchView();
        configurationDrawerLayout();
        configurationNavigationView();
        configurationSpinner();
        startAlarm();

    }


    private void startAlarm() {
        Log.d(TAG, "startAlarm: Function");
        WordViewModel wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        List<Word> words = wordViewModel.getAllIsReminded();
        if (words.size() > 0) {
            Log.d(TAG, "startAlarm: if cond");
            AlarmHelper alarmManagement = new AlarmHelper(this, (ArrayList<Word>) words);
            alarmManagement.setAlarm();
        }
    }

    private void configurationSpinner() {
        List<String> list = new ArrayList<>();
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (list.size() > 0) {
                    list.clear();
                }
                for (Category category : categories
                ) {
                    list.add(category.getTitle());
                }
                list.add(0,"All");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
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
    public void setToolbar() {
        binding.appBarMain.setVisibility(View.INVISIBLE);
        binding.searchView.onActionViewCollapsed();

    }

    @Override
    public void resetToolbar() {
        binding.appBarMain.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null)
            binding = null;
    }


}