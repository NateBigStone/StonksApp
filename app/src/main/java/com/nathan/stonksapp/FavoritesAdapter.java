package com.nathan.stonksapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private String TAG = "Favorites_Adapter";
    private List<Stock> data;
    private int length;

    private FavoritesClickListener listener;

    public FavoritesAdapter(List<Stock> data, int length, FavoritesClickListener listener) {
        this.listener = listener;
        this.data = data;
        this.length = length;
    }

    static class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        LinearLayout layout;
        TextView symbolView;
        TextView lastUpdatedView;
        TextView priceView;
        ImageView stockPhotoView;
        FavoritesClickListener listener;

        FavoritesViewHolder(LinearLayout layout, FavoritesClickListener listener) {
            super(layout);
            this.listener = listener;
            this.layout = layout;
            symbolView = layout.findViewById(R.id.symbol_recycle);
            lastUpdatedView = layout.findViewById(R.id.last_updated);
            priceView = layout.findViewById(R.id.price_recycle);
            stockPhotoView = layout.findViewById(R.id.stock_photo);

            layout.setOnClickListener(this);
            layout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view){

            listener.onListClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view){
            listener.onListLongClick(getAdapterPosition());
            return true;
        }
    }

    @NonNull
    @Override
    public FavoritesAdapter.FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_element, parent, false);

        //Create a new viewHolder, to contain this TextView
        FavoritesViewHolder viewHolder = new FavoritesViewHolder(layout, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoritesViewHolder holder, int position) {

        Stock stock = data.get(position);
        holder.symbolView.setText(stock.getSymbol());
        holder.priceView.setText(stock.getPrice());
        holder.lastUpdatedView.setText("Last Updated: " + new Date(Long.parseLong(stock.getLastUpdated())));
    }

    @Override
    public int getItemCount() {
        return this.length;
    }

}