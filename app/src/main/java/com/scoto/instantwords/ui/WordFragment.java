package com.scoto.instantwords.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.scoto.instantwords.R;
import com.scoto.instantwords.data.model.Word;
import com.scoto.instantwords.databinding.FragmentWordBinding;
import com.scoto.instantwords.viewmodel.WordViewModel;


public class WordFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "WordFragment";
    private FragmentWordBinding wordBinding;
    private WordViewModel wordViewModel;
    private Word word;
    private Word changedWord;
    private View view;
    private String wordBg = "";
    private String bg = "";

    public WordFragment() {
        //Empty Constructor required
    }

    public static WordFragment newInstance(Word word) {
        Bundle args = new Bundle();
        args.putParcelable("DATA", word);
        WordFragment fragment = new WordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        wordBinding = FragmentWordBinding.inflate(inflater, container, false);
        view = wordBinding.getRoot();
        configurationToolbar();
        setListeners();

        word = getArguments().getParcelable("DATA");
        wordBinding.setWord(word);


        wordBg = word.getBgColor();//Layout Background Color
        bg = word.getBgColor();//use, when checking the changes when leaving from add word fragment.

        return view;
    }

    private void setListeners() {
        wordBinding.save.setOnClickListener(this::onClick);
        wordBinding.share.setOnClickListener(this::onClick);
        wordBinding.delete.setOnClickListener(this::onClick);
        wordBinding.relativeLayoutContent.setOnClickListener(this::onClick);
        //Color Group
        wordBinding.red.setOnClickListener(this::onClick);
        wordBinding.blue.setOnClickListener(this::onClick);
        wordBinding.purple.setOnClickListener(this::onClick);
        wordBinding.yellow.setOnClickListener(this::onClick);
        wordBinding.green.setOnClickListener(this::onClick);
    }

    private void configurationToolbar() {
        Toolbar toolbar = wordBinding.toolbarWord;
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        onDetach();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.save:
                saveWord();
                break;
            case R.id.share:
                shareWord();
                break;
            case R.id.delete:
                deleteWord();
                break;
            case R.id.red:
                setLayoutBg("#E74C3C");
                break;
            case R.id.green:
                setLayoutBg("#2ecc71");
                break;
            case R.id.blue:
                setLayoutBg("#2980B9");
                break;
            case R.id.yellow:
                setLayoutBg("#F1C40F");
                break;
            case R.id.purple:
                setLayoutBg("#9b59b6");
                break;
        }
    }

    private void closeFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void saveWord() {
        Log.d(TAG, "saveWord: Save Word");

        String wordTxt = wordBinding.word.getText().toString();
        String definitionTxt = wordBinding.definition.getText().toString();

        word.setWord(wordTxt);
        word.setDefinition(definitionTxt);
        word.setBgColor(wordBg);

        wordViewModel.update(word);

        Toast.makeText(getContext(), getString(R.string.changes_saved), Toast.LENGTH_SHORT).show();
        onDetach();

    }

    private void shareWord() {
        String shareFormat = word.getWord() + "\n" + word.getDefinition();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareFormat);
        shareIntent.setType("text/plain");
        getContext().startActivity(Intent.createChooser(shareIntent, "WORD"));

    }

    private void deleteWord() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                wordViewModel.delete(word);
                Toast.makeText(getContext(), getString(R.string.deletion), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                closeFragment();
            }
        });
        builder.setNegativeButton(R.string.dialog_negative, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void setLayoutBg(String colorCode) {
        wordBg = colorCode;//Updated layout bg color with new color code.
        if (colorCode != "") {
            wordBinding.relativeLayoutContent.setBackgroundColor(Color.parseColor(colorCode));
        }
    }



    private boolean isChanged() {
        String wordText = wordBinding.word.getText().toString();
        String definitionText = wordBinding.definition.getText().toString();
        if (!word.getWord().equals(wordText)
                || !word.getDefinition().equals(definitionText)
                || bg != wordBg) {
            return false;
        }
        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //if soft keyboard is active, close keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        //When entered to fragment
        Log.d(TAG, "onResume: Word Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        //When leaving from fragment
        //Checks if data is changed, if its then save changes.
        //Some users may forget to save changes.

        if (!isChanged()) {
            //Save changes before leaving.
            Log.d(TAG, "onPause: Data is changed, save them before leaving.");
            //There is two way to save changes
            //one,user can save with 'save' btn,
            //second, app save automatically save when closing/leaving fragment.
            //Here second one.
            saveWord();

        } else {
            // do nothing
        }
        Log.d(TAG, "onPause: Word Fragment");
    }
}