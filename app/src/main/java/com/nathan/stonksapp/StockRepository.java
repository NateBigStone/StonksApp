package com.nathan.stonksapp;
import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

public class StockRepository {

    private StockDAO mStockDAO;

    public StockRepository(Application application) {
        StockDatabase db = StockDatabase.getDatabase(application);
        mStockDAO = db.mStockDAO();
    }

    public void insert(Stock record) {
        new InsertStockAsync(mStockDAO).execute(record);
    }

    static class InsertStockAsync extends AsyncTask<Stock, Void, Void> {

        private StockDAO mStockDAO;

        InsertStockAsync(StockDAO mStockDAO) {
            this.mStockDAO = mStockDAO;
        }

        @Override
        protected Void doInBackground(Stock... stockList) {
            mStockDAO.insert(stockList);
            return null;
        }
    }

    public void delete(Stock record){
        new DeleteStockAsync(mStockDAO).execute(record);
    }

    static class DeleteStockAsync extends AsyncTask<Stock, Void, Void> {

        private StockDAO mStockDAO;

        DeleteStockAsync(StockDAO mStockDAO) {
            this.mStockDAO = mStockDAO;
        }

        @Override
        protected Void doInBackground(Stock... stockList) {
            mStockDAO.delete(stockList);
            return null;
        }

    }

    public void update(Stock record) {

        new UpdateStockAsync(mStockDAO).execute(record);

    }

    static class UpdateStockAsync extends AsyncTask<Stock, Void, Void> {

        private StockDAO mStockDAO;

        UpdateStockAsync(StockDAO mStockDAO) {
            this.mStockDAO = mStockDAO;
        }

        @Override
        protected Void doInBackground(Stock... stockList) {
            mStockDAO.update(stockList);
            return null;
        }

    }

    public LiveData<List<Stock>> getAllRecords() {
        return mStockDAO.getAllRecords();
    }

    public LiveData<Stock> getRecordForSymbol(String symbol) {
        return mStockDAO.getRecordForTitle(symbol);
    }

    public LiveData<Stock> getRecordForLastUpdated(String date) {
        return mStockDAO.getRecordForLastUpdated(date);
    }

    public LiveData<Integer> getRowCount() {
        return mStockDAO.getRowCount();
    }
}

