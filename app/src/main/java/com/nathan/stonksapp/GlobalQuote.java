package com.nathan.stonksapp;
import com.fasterxml.jackson.annotation.JsonAlias;

public class GlobalQuote {

    @JsonAlias({"symbol", "01. symbol"})
    public String symbol;
    @JsonAlias({"open", "02. open"})
    public String open;
    @JsonAlias({"high", "03. high"})
    public String high;
    @JsonAlias({"low", "04. low"})
    public String low;
    @JsonAlias({"price", "05. price"})
    public String price;
    @JsonAlias({"volume", "06. volume"})
    public String volume;
    @JsonAlias({"latestTradingDay", "07. latest trading day"})
    public String latestTradingDay;
    @JsonAlias({"previousClose", "08. previous close"})
    public String previousClose;
    @JsonAlias({"change", "09. change"})
    public String change;
    @JsonAlias({"changePercent", "10. change percent"})
    public String changePercent;

    @Override
    public String toString() {
        return "{" +
                "\"symbol\":\""+ symbol +"\"," +
                "\"open\":\""+ open +"\"," +
                "\"high\":\""+ high +"\"," +
                "\"low\":\""+ low +"\"," +
                "\"price\":\""+ price +"\"," +
                "\"volume\":\""+ volume +"\"," +
                "\"latestTradingDay\":\""+ latestTradingDay +"\"," +
                "\"previousClose\":\""+ previousClose +"\"," +
                "\"change\":\""+ change +"\",\n" +
                "\"changePercent\":\""+ changePercent +"\"" +
                "}";
    }
}