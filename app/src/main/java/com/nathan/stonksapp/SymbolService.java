package com.nathan.stonksapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SymbolService {

    @GET("query")
    Call<Symbol> getPrice(@Query("function") String mQuery, @Query("symbol") String mSymbol, @Query("apikey") String mKey);
}