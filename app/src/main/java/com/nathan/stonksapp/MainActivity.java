package com.nathan.stonksapp;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Timer;
import java.util.TimerTask;

//Icons from icons8

public class MainActivity extends AppCompatActivity{

    private Button mSearchButton;
    private EditText mEnterSymbol;

    private FragmentManager fm;
    private FragmentTransaction ft;

    // Create the frags
    private FavoritesFragment mFavoritesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchButton = findViewById(R.id.search_button);
        mEnterSymbol = findViewById(R.id.enter_symbol);

        mFavoritesFragment = FavoritesFragment.newInstance();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.action_fragment, mFavoritesFragment);
        ft.commit();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mSymbol = mEnterSymbol.getText().toString();
                if (mSymbol.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter a Symbol", Toast.LENGTH_SHORT).show();
                    return;
                }
                hideKeyboard();
                onSendStock(mSymbol);
                mEnterSymbol.setText(R.string.empty);

            }
        });

        //https://stackoverflow.com/questions/11434056/how-to-run-a-method-every-x-seconds
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mFavoritesFragment.updateFavorites();
            }
        }, 10000, 300000);
    }


    public void onSendStock(String mStock) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ResultFragment mResultFragment = ResultFragment.newInstance(mStock);
        ft.replace(R.id.action_fragment, mResultFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void hideKeyboard() {
        View mainView = findViewById(android.R.id.content);
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(mainView.getWindowToken(), 0);
    }

}
