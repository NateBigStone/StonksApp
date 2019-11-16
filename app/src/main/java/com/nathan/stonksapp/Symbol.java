package com.nathan.stonksapp;


import com.fasterxml.jackson.annotation.JsonAlias;

public class Symbol {
    @JsonAlias({"global_quote", "Global Quote"})
    public String global_quote;
//    @JsonAlias({"symbol", "01. symbol"})
//    public String symbol;
//    public String open;
//    public String high;
//    public String low;
//    public String price;
//    public String volume;
//    public String latest_trading_day;
//    public String previous_close;
//    public String change;
//    public String change_percent;

//    @Override
//    public String toString() {
//        return "Symbol{\"Global Quote\":{" +
//                "\"Error Message\":\""+ error +"\","+
//                "}}";
//    }
}

//        return "Symbol{\"Global Quote\":{" +
//                "\"01. symbol\":\""+ symbol +"\"," +
//                "\"02. open\":\""+ open +"\"," +
//        "\"03. high\":\""+ high +"\"," +
//        "\"04. low\":\""+ low +"\"," +
//        "\"05. price\":\""+ price +"\"," +
//        "\"06. volume\":\""+ volume +"\"," +
//        "\"07. latest trading day\":\""+ latest_trading_day +"\"," +
//        "\"08. previous close\":\""+ previous_close +"\"," +
//        "\"09. change\":\""+ change +"\",\n" +
//        "\"10. change percent\":\""+ change_percent +"\"" +
//        "}}";
