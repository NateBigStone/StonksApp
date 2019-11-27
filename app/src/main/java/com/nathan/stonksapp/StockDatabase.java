package com.nathan.stonksapp;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Stock.class}, version = 1, exportSchema = false)
public abstract class StockDatabase extends RoomDatabase {

    private static volatile StockDatabase INSTANCE;

    public abstract StockDAO mStockDAO();

    static StockDatabase getDatabase(final Context context) {

        if(INSTANCE == null) {
            synchronized (StockDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StockDatabase.class, "Stock").enableMultiInstanceInvalidation().build();
                }
            }
        }
        return INSTANCE;
    }
}
