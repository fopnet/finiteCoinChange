package com.change.exception;

import java.util.Queue;
import com.change.Stock.Coin;

public class ChangeNotPossibleException extends RuntimeException {
    
    public ChangeNotPossibleException(Double value, Queue<Coin> coins) {
        super(String.format("It is not possible to return change %d related to stock:", getMessage(coins)));
    }

    private static String getMessage(Queue<Coin> coins) {
        return coins.stream().reduce(new StringBuilder(""),
        (sb, it) -> new StringBuilder(String.format("%d coins of %d", it.getAmount(), it.getValue())),
        StringBuilder::append).toString();

    }
}
