package com.nathan.stonksapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface StockDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Stock... wr);

    @Update
    void update(Stock... wr);

    @Delete
    void delete(Stock... wr);

    @Query("SELECT * FROM Stock WHERE symbol = :symbol LIMIT 1")
    LiveData<Stock> getRecordForSymbol(String symbol);

    @Query("SELECT * FROM Stock WHERE lastUpdated = :lastUpdated LIMIT 1")
    LiveData<Stock> getRecordForLastUpdated(String lastUpdated);

    @Query("SELECT * FROM Stock ORDER BY symbol ASC")
    LiveData<List<Stock>> getAllRecords();

    @Query("SELECT COUNT(symbol) FROM Stock")
    LiveData<Integer> getRowCount();

}
