package com.nathan.stonksapp;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ResultFragment extends Fragment {

    //Arg things
    private final static String ARG_RESPONSE = "response";
    private String mStockTicker;

    //Response things
    private Symbol mSymbolResponse;

    //Element things
    private TextView mSymbol;
    private TextView mPrice;
    private TextView mOpenLabel;
    private TextView mOpen;
    private TextView mHighLabel;
    private TextView mHigh;
    private TextView mLowLabel;
    private TextView mLow;
    private TextView mVolumeLabel;
    private TextView mVolume;
    private TextView mLatestTradingDayLabel;
    private TextView mLatestTradingDay;
    private TextView mPreviousCloseLabel;
    private TextView mPreviousClose;
    private TextView mChangeLabel;
    private TextView mChange;
    private TextView mChangePercentLabel;
    private TextView mChangePercent;
    private Button mSaveButton;
    private Button mBackButton;

    //Logging thing
    private static final String TAG = "RESULT_STONK";

    //Database things
    private SymbolService mSymbolService;
    private StockViewModel mStockDatabase;
    private Stock mStockQuery;

    //Other frag
    private FavoritesFragment mFavoritesFragment;

    public ResultFragment() {
        // Required empty public constructor
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
        mOpenLabel = view.findViewById(R.id.open_label);
        mOpen = view.findViewById(R.id.open);
        mHighLabel = view.findViewById(R.id.high_label);
        mHigh = view.findViewById(R.id.high);
        mLowLabel = view.findViewById(R.id.low_label);
        mLow = view.findViewById(R.id.low);
        mVolumeLabel = view.findViewById(R.id.volume_label);
        mVolume = view.findViewById(R.id.volume);
        mLatestTradingDayLabel = view.findViewById(R.id.latest_trading_day_label);
        mLatestTradingDay = view.findViewById(R.id.latest_trading_day);
        mPreviousCloseLabel = view.findViewById(R.id.previous_close_label);
        mPreviousClose = view.findViewById(R.id.previous_close);
        mChangeLabel = view.findViewById(R.id.change_label);
        mChange = view.findViewById(R.id.change);
        mChangePercentLabel = view.findViewById(R.id.change_percent_label);
        mChangePercent = view.findViewById(R.id.change_percent);
        mSaveButton = view.findViewById(R.id.save_to_favorites);
        mSaveButton.setVisibility(View.GONE);
        mBackButton = view.findViewById(R.id.back_button);
        mBackButton.setVisibility(View.GONE);

        //Button to save to favorites
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               saveToFavorites(mSymbolResponse);
               goBack();
            }
        });

        //Button to go back to favorites
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                goBack();
            }
        });

        return view;
    }

    private void getPrice(final String mSymbol) {
        String mQuery = "GLOBAL_QUOTE";
        mSymbolService.getPrice(mQuery, mSymbol, BuildConfig.API_KEY).enqueue(new Callback<Symbol>() {
            @Override
            public void onResponse(@NonNull Call<Symbol> call, @NonNull Response<Symbol> response) {
                mSymbolResponse = response.body();
                if (mSymbolResponse != null) {
                    //Set the texts
                    setTexts(mSymbolResponse);
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
        mPrice.setText(mSymbolResponse.globalQuote.price.substring(0, mSymbolResponse.globalQuote.price.length() - 2));
        mOpenLabel.setText(R.string.open_label);
        mOpen.setText(mSymbolResponse.globalQuote.open);
        mHighLabel.setText(R.string.high_label);
        mHigh.setText(mSymbolResponse.globalQuote.high);
        mLowLabel.setText(R.string.low_label);
        mLow.setText(mSymbolResponse.globalQuote.low);
        mVolumeLabel.setText(R.string.volume_label);
        mVolume.setText(mSymbolResponse.globalQuote.volume);
        mLatestTradingDayLabel.setText(R.string.latest_trading_day_label);
        mLatestTradingDay.setText(mSymbolResponse.globalQuote.latestTradingDay);
        mPreviousCloseLabel.setText(R.string.previous_close_label);
        mPreviousClose.setText(mSymbolResponse.globalQuote.previousClose);
        mChangeLabel.setText(R.string.change_label);
        mChange.setText(mSymbolResponse.globalQuote.change);
        mChangePercentLabel.setText(R.string.change_percent_label);
        mChangePercent.setText(mSymbolResponse.globalQuote.changePercent);
        mSaveButton.setVisibility(View.VISIBLE);
        mBackButton.setVisibility(View.VISIBLE);
    }

    private void saveToFavorites(final Symbol mSymbolResponse){
        //Save to database
        final Stock mNewStock = new Stock(
                mSymbolResponse.globalQuote.symbol,
                Long.toString(new Date().getTime()),
                mSymbolResponse.globalQuote.open,
                mSymbolResponse.globalQuote.high,
                mSymbolResponse.globalQuote.low,
                mSymbolResponse.globalQuote.price,
                mSymbolResponse.globalQuote.volume,
                mSymbolResponse.globalQuote.latestTradingDay,
                mSymbolResponse.globalQuote.previousClose,
                mSymbolResponse.globalQuote.change,
                mSymbolResponse.globalQuote.changePercent);

        // From https://stackoverflow.com/questions/44167111/android-room-simple-select-query-cannot-access-database-on-the-main-thread
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mStockQuery = mStockDatabase.getRecordForSymbol(mSymbolResponse.globalQuote.symbol);
                if (mStockQuery == null) {
                    mStockDatabase.insert(mNewStock);
                    Log.d(TAG, "The database entry is saved not updated: " + mStockQuery + " : " + mSymbolResponse.globalQuote.symbol);
                }
                else{
                    mStockDatabase.update(mNewStock);
                    //Toast.makeText(getContext(),mSymbolResponse.globalQuote.symbol + " updated", Toast.LENGTH_LONG).show();
                }
            }
        });
        Toast.makeText(getContext(),mSymbolResponse.globalQuote.symbol + " saved to favorites", Toast.LENGTH_LONG).show();
    }

    private void goBack() {
        mFavoritesFragment = FavoritesFragment.newInstance();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.action_fragment, mFavoritesFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
