package com.nathan.stonksapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

//TODO: Send response to fragment
//TODO: Be able to send Fragment empty string
//TODO: Loading bar for fragment
//TODO: Add to favorites button


public class ResultFragment extends Fragment {

    //Arg things
    private final static String ARG_RESPONSE = "response";
    private String mStockTicker;

    //Element things
    private TextView mSymbol;
    private TextView mPrice;
    private TextView mLastUpdated;
    private TextView mOpen;

    //Logging thing
    private static final String TAG = "RESULT_STONK";

    //Database things
    private SymbolService mSymbolService;
    private StockViewModel mStockDatabase;


    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(){
        return new ResultFragment();
    }


    public static ResultFragment newInstance(String mStockResponse) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESPONSE, mStockResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

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

        mStockDatabase = new StockViewModel(getActivity().getApplication());
        mSymbolService = retrofit.create(SymbolService.class);

        if(getArguments() != null) {
            mStockTicker = getArguments().getString(ARG_RESPONSE);
            getPrice(mStockTicker);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.result_fragment, container, false);
        // Inflate the layout for this fragment
        mSymbol = view.findViewById(R.id.symbol);
        mPrice = view.findViewById(R.id.price);
        mLastUpdated = view.findViewById(R.id.last_updated);
        mOpen = view.findViewById(R.id.open);


        //TODO: Create a button to save to favorites

        return view;
    }

    private void getPrice(final String mSymbol) {
        String mQuery = "GLOBAL_QUOTE";
        mSymbolService.getPrice(mQuery, mSymbol, BuildConfig.API_KEY).enqueue(new Callback<Symbol>() {
            @Override
            public void onResponse(@NonNull Call<Symbol> call, @NonNull Response<Symbol> response) {
                Symbol mSymbolResponse = response.body();
                if (mSymbolResponse != null) {
                    //Set the texts
                    setTexts(mSymbolResponse);

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
                    Toast.makeText(getContext(),mSymbolResponse.globalQuote.symbol + " saved to favorites", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getContext(),"Unable to get information", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Symbol> call, Throwable t) {
                Log.e(TAG, "Error getting info", t);
                Toast.makeText(getContext(),"Unable to get information", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setTexts(final Symbol mSymbolResponse){
        mSymbol.setText(mSymbolResponse.globalQuote.symbol);
        mPrice.setText(mSymbolResponse.globalQuote.price);
        mLastUpdated.setText("");
        mOpen.setText(mSymbolResponse.globalQuote.open);
    }

}
