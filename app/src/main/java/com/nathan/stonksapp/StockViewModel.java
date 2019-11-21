package com.nathan.stonksapp;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class StockViewModel extends AndroidViewModel {

    private StockRepository repository;

    private LiveData<List<Stock>> allRecords;

    private LiveData<Integer> rowCount;

    public StockViewModel(@NonNull Application application) {
        super(application);
        repository = new StockRepository(application);
        allRecords = repository.getAllRecords();
        rowCount = repository.getRowCount();
    }

    public LiveData<List<Stock>> getAllRecords() {
        return allRecords;
    }

    public LiveData<Stock> getRecordForSymbol(String title) {
        return repository.getRecordForSymbol(title);
    }

    public LiveData<Stock> getRecordForLastUpdated(String date) {
        return repository.getRecordForLastUpdated(date);
    }

    public LiveData<Integer> getRowCount(){
        return rowCount;
    }

    public void insert(Stock record) {
        repository.insert(record);
    }

    public void delete(Stock record) {
        repository.delete(record);
    }

    public void update(Stock record) {
        repository.update(record);
    }
}