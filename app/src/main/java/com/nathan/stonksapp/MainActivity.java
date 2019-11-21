package com.nathan.stonksapp;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MAIN_STONK";

    private Button mSearchButton;
    private EditText mEnterSymbol;

    //Database things
    private SymbolService mSymbolService;
    private StockViewModel mStockDatabase;

    // Create the purple frag
    private ResultFragment mResultFragment;

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

        mStockDatabase = new StockViewModel(getApplication());
        mSymbolService = retrofit.create(SymbolService.class);

        mSearchButton = findViewById(R.id.search_button);
        mEnterSymbol = findViewById(R.id.enter_symbol);

        mResultFragment = ResultFragment.newInstance("");
        //mResultFragment.setOnStockListener(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.action_fragment, mResultFragment);
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
                getPrice(mSymbol);
            }
        });
    }

    private void getPrice(final String mSymbol) {
        String mQuery = "GLOBAL_QUOTE";
        mSymbolService.getPrice(mQuery, mSymbol, BuildConfig.API_KEY).enqueue(new Callback<Symbol>() {
            @Override
            public void onResponse(@NonNull Call<Symbol> call, @NonNull Response<Symbol> response) {
                Symbol mSymbolResponse = response.body();
                if (mSymbolResponse != null) {

                    //Save to database
                    Stock mNewStock = new Stock(
                            mSymbolResponse.globalQuote.symbol,
                            mSymbolResponse.globalQuote.open,
                            mSymbolResponse.globalQuote.high,
                            mSymbolResponse.globalQuote.low,
                            mSymbolResponse.globalQuote.price,
                            mSymbolResponse.globalQuote.volume,
                            mSymbolResponse.globalQuote.latestTradingDay,
                            mSymbolResponse.globalQuote.previousClose,
                            mSymbolResponse.globalQuote.change,
                            mSymbolResponse.globalQuote.changePercent);
                    mStockDatabase.insert(mNewStock);
                    //TODO: Remove Log.d
                    Log.d(TAG, "The database is: " + mStockDatabase);
                    Toast.makeText(MainActivity.this,mSymbolResponse.globalQuote.symbol + " saved to favorites", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(MainActivity.this,"Unable to get information", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Symbol> call, Throwable t) {
                Log.e(TAG, "Error getting info", t);
                Toast.makeText(MainActivity.this,"Unable to get information", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onStock(String mStockResponse) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ResultFragment mResultFragment = ResultFragment.newInstance("");
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
