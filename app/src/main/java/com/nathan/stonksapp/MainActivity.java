package com.nathan.stonksapp;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_STONK";

    Button mSearchButton;
    EditText mEnterSymbol;
    TextView mLatestPrice;

    SymbolService mSymbolService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrofit Debugging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();

        mSymbolService = retrofit.create(SymbolService.class);

        mSearchButton = findViewById(R.id.search_button);
        mEnterSymbol = findViewById(R.id.enter_symbol);
        mLatestPrice = findViewById(R.id.latest_price);

        // Hide price until one is available
        mLatestPrice.setVisibility(GONE);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mSymbol = mEnterSymbol.getText().toString();
                if (mSymbol.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter a Symbol", Toast.LENGTH_SHORT).show();
                    return;
                }

                hideKeyboard();
                getPrice(mSymbol);
            }
        });
    }

    private void getPrice(final String mSymbol) {
        String mQuery = "GLOBAL_QUOTE";
        String mKey = "&apikey=" + BuildConfig.API_KEY;
        mSymbolService.getPrice(mQuery, mSymbol, BuildConfig.API_KEY).enqueue(new Callback<Symbol>() {
            @Override
            public void onResponse(@NonNull Call<Symbol> call, @NonNull Response<Symbol> response) {
                Symbol mSymbolResponse = response.body();
                Log.d(TAG, "Symbol Response: " + mSymbolResponse);

                if (mSymbolResponse != null) {
                    mLatestPrice.setVisibility(View.VISIBLE);
                    mLatestPrice.setText(mSymbolResponse.globalQuote.price);
                }
                else {
                    Toast.makeText(MainActivity.this,"Unable to get information", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Symbol> call, Throwable t) {
                Log.e(TAG, "Error getting info", t);
                Toast.makeText(MainActivity.this,"Unable to get information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyboard() {
        View mainView = findViewById(android.R.id.content);
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(mainView.getWindowToken(), 0);
    }

}
