package com.nathan.stonksapp;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

        Log.d(TAG, "The database is: " + mStockDatabase);

        mStockDatabase.getAllRecords().observe(this, new Observer<List<Stock>>() {

            @Override
            public void onChanged(List<Stock> favorites) {
                mFavoritesList = favorites;
                mLength = favorites.size();
                Log.d(TAG, "Stock records are: " + favorites);
                mAdapter = new FavoritesAdapter(mFavoritesList, mLength, FavoritesFragment.this);
                mFavoritesRecyclerView.setAdapter(mAdapter);
            }
        });

        Log.d(TAG, "Stock records are: " + mFavoritesList);
        mFavoritesRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplication());
        mFavoritesRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FavoritesAdapter(mFavoritesList, mLength,this);
        mFavoritesRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onListClick(int position) {
        System.out.println(position);
        Stock stock;
        stock = mFavoritesList.get(position);
        System.out.println(stock);
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
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        confirmDeleteDialog.show();
    }

}
