package com.nathan.stonksapp;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class FavoritesFragment extends Fragment implements FavoritesClickListener{

    //Element things
    private RecyclerView mFavoritesRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //Logging thing
    private static final String TAG = "FAVORITES_STONK";

    //Database things
    private StockViewModel mStockDatabase;

    private List<Stock> mFavoritesList;
    private String mTicker;
    private int mLength;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        mStockDatabase = new StockViewModel(getActivity().getApplication());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_fragment, container, false);
        // Inflate the layout for this fragment
        mFavoritesRecyclerView = view.findViewById(R.id.favorites_list);

        mStockDatabase.getAllRecords().observe(this, new Observer<List<Stock>>() {

            @Override
            public void onChanged(List<Stock> favorites) {
                mFavoritesList = favorites;
                mLength = favorites.size();
                mAdapter = new FavoritesAdapter(mFavoritesList, mLength, FavoritesFragment.this);
                mFavoritesRecyclerView.setAdapter(mAdapter);
            }
        });

        mFavoritesRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplication());
        mFavoritesRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FavoritesAdapter(mFavoritesList, mLength,this);
        mFavoritesRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onListClick(int position) {
        Stock stock;
        stock = mFavoritesList.get(position);
        mTicker = stock.getSymbol();
        ((MainActivity)getActivity()).onSendStock(mTicker);


    }
    @Override
    public void onListLongClick(int position) {
        final Stock deleteFavorite;
        deleteFavorite = mFavoritesList.get(position);

        AlertDialog confirmDeleteDialog = new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.delete_favorite_message, deleteFavorite.getSymbol() ))
                .setTitle(getString(R.string.delete_dialog_title))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //remove from database
                        mStockDatabase.delete(deleteFavorite);
                        Toast.makeText(getContext(),deleteFavorite.getSymbol() + " removed from favorites", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        confirmDeleteDialog.show();
    }

    public void updateFavorites(){

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

        SymbolService mSymbolService = retrofit.create(SymbolService.class);
        String mQuery = "GLOBAL_QUOTE";

        final ResultFragment mResultFragment = ResultFragment.newInstance();

        if (mFavoritesList != null) {
            for (int i=0; i< mFavoritesList.size(); i++) {
                String mIterateTick = mFavoritesList.get(i).getSymbol();
                mSymbolService.getPrice(mQuery, mIterateTick, BuildConfig.API_KEY).enqueue(new Callback<Symbol>() {
                    @Override
                    public void onResponse(@NonNull Call<Symbol> call, @NonNull Response<Symbol> response) {
                        Symbol mSymbolResponse = response.body();
                        Log.d("Response_body", mSymbolResponse.globalQuote.symbol);
                        //if api response is valid
                        if (mSymbolResponse != null) {
                            mResultFragment.saveToFavorites(mSymbolResponse);
                            System.out.println("Updating: " + mSymbolResponse.globalQuote.symbol);
                        }
                    }
                    @Override
                    public void onFailure(Call<Symbol> call, Throwable t) {
                        Log.e(TAG, "Error getting info", t);
                    }
                });
                // Alpha Vantage standard API call frequency is 5 calls per minute and 500 calls per day
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
