package com.change;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.apache.log4j.Logger;
import com.change.Stock.Coin;
import com.change.exception.ChangeNotPossibleException;

public class FiniteCoinChange {
    static final Logger logger = Logger.getLogger(FiniteCoinChange.class);

    public static void main(String[] args) {
        Double valor = 11d;
        Coin coin2 = new Coin(2, 4);
        Coin coin3 = new Coin(3, 1);

        Stock stock = new Stock(asList(coin2, coin3));

        FiniteCoinChange finiteCoinChange = new FiniteCoinChange(); 
        List<Coin> change = finiteCoinChange.minCoins(valor, stock);
        finiteCoinChange.printChange(change);

    }

    public List<Coin> minCoins(Double value, Stock stock) {
        return minCoins(value, stock, stock.getQueue(value), new ArrayList<>());
    }

    private List<Coin> minCoins(Double value, Stock stock, Queue<Coin> queue, List<Coin> change) {
        if (Double.valueOf(0).equals(value)) {
            return change;
        }

        Coin coin = queue.peek();
        if (isNull(coin) || value < coin.getValue()) {
            throw new ChangeNotPossibleException(value, stock.getQueue(value));
        }

        // multiple test
        int divisor = (int)(value / coin.getValue());
        Coin debitInStock = null;
        if (divisor <= coin.getAmount()) {
            value -= (int) (coin.getValue() * divisor);
            debitInStock = new Coin(coin.getValue(), divisor);
        } else { // decrement current coin
            value -= coin.getValue();
            debitInStock = new Coin(coin.getValue(), 1);
        }
        
        // coin.decrement(debitInStock.getAmount());
        change.add(debitInStock);
        stock.remove(debitInStock);

        return minCoins(value, stock, stock.getQueue(value), change);
    }


    void printChange(List<Coin> change) {
        StringBuilder msg = change.stream().reduce(new StringBuilder("O troco do cliente será: "),
                (sb, it) ->  sb.append (new StringBuilder(String.format("%d moedas de %d", it.getAmount(), it.getValue())).append( " e ")),
                (sb1, sb2) -> sb1);

        logger.info(msg.substring(0, msg.toString().length() - 3));
    }

}
