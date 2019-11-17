package com.nathan.stonksapp;
import com.fasterxml.jackson.annotation.JsonAlias;

public class Symbol {
    @JsonAlias({"globalQuote", "Global Quote"})
    public GlobalQuote globalQuote;
        @Override
        public String toString() {
            return "Symbol{\"qlobalQuote\":" +
                    globalQuote +
                    "}";
    }
}
