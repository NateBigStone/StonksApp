package com.nathan.stonksapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SymbolService {

    @GET("{mGlobalQuote}")
    Call<Symbol> getPrice(@Path("mGlobalQuote") String mGlobalQuote);
}
//TODO: separate the query, key