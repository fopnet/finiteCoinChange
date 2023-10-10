package com.change.exception;

import com.change.Stock.Coin;

public class IllegalStockAccessException extends RuntimeException {
    
    public IllegalStockAccessException(Coin coin) {
        super(String.format("Coin %d not found", coin.getValue()));
    }
}
