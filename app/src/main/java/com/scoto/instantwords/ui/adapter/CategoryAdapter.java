package com.scoto.instantwords.ui.adapter;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.scoto.instantwords.data.model.Category;
import com.scoto.instantwords.databinding.CategoryItemBinding;
import com.scoto.instantwords.utils.interfaces.IDialogDismiss;
import com.scoto.instantwords.viewmodel.CategoryViewModel;
import com.scoto.instantwords.viewmodel.WordViewModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private static final String TAG = "CategoryAdapter";
    private CategoryViewModel categoryViewModel;
    private WordViewModel wordViewModel;
    private List<Category> categoryList;
    private Context context;
    private Fragment fragment;
    private IDialogDismiss dialogDismiss;


    public CategoryAdapter(Fragment fragment, IDialogDismiss dialogDismiss) {
        this.dialogDismiss = dialogDismiss;
        this.fragment = fragment;
        categoryViewModel = new ViewModelProvider(fragment).get(CategoryViewModel.class);
        wordViewModel = new ViewModelProvider(fragment).get(WordViewModel.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CategoryItemBinding itemBinding = CategoryItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(itemBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //To disable edit and delete first category item in Categories. ---->'All'
        if (position == 0 || categoryList.get(position).getTitle().equals("All")) {
            holder.binding.editCategoryTitle.setVisibility(View.INVISIBLE);
            holder.binding.deleteCategory.setVisibility(View.INVISIBLE);

            holder.binding.categoryTitle.setFocusable(false);
            holder.binding.categoryTitle.setFocusableInTouchMode(false);
            holder.binding.categoryTitle.setInputType(InputType.TYPE_NULL);
        }

        final Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CategoryItemBinding binding;

        public ViewHolder(CategoryItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;

            itemBinding.editCategoryTitle.setOnClickListener(v -> {


                String newTitle = itemBinding.categoryTitle.getText().toString();
                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                int id = categoryList.get(getLayoutPosition()).getId();

                Category category = new Category(newTitle);
                category.setId(id);
                categoryViewModel.update(category);
                dialogDismiss.onDismiss();
            });
            itemBinding.deleteCategory.setOnClickListener(v -> {
                Log.d(TAG, "ViewHolder: Delete Category");
                String title = categoryList.get(getLayoutPosition()).getTitle();
                categoryViewModel.delete(categoryList.get(getLayoutPosition()));
                Toast.makeText(context, title + " deleted.", Toast.LENGTH_SHORT).show();
            });
        }

        public void bind(Category category) {
            binding.setCategory(category);
            binding.executePendingBindings();
        }
    }
}
