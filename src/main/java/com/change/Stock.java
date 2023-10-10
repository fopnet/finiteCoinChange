package com.change;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import com.change.exception.IllegalStockAccessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class Stock {

    private Set<Coin> coins;

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Coin {
        @EqualsAndHashCode.Include
        private int value;
        private int amount;

        void decrement(int amount) {
            if (this.amount > amount) {
                throw new IllegalArgumentException("There are not enough amount to decrement");
            }
            this.amount -= amount;
        }

        void increment(int amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Invalid amount to increment.");
            }
            this.amount += amount;
        }

        boolean isValueLess() {
            return getAmount() == 0;
        }
    }


    public Stock(List<Coin> coins) {
        coins.sort(Comparator.comparing(Coin::getValue).reversed());
        this.coins = new LinkedHashSet<>(coins);
    }

    public Coin add(final Coin coin) {
        Optional<Coin> opt = find(coin);
        if (opt.isPresent()) {
            Coin found = opt.get();
            found.increment(coin.getAmount());
            return found;
        } else {
            coins.add(coin);
            return coin;
        }
    }

    public Coin remove(Coin coin) {
        Optional<Coin> opt = find(coin);
        if (opt.isPresent()) {
            Coin found = opt.get();
            found.decrement(coin.getAmount());
            if (found.isValueLess()) {
                coins.remove(found);
            }
            return found;
        }
        throw new IllegalStockAccessException(coin);
    }

    public Optional<Coin> find(Coin coin) {
        return coins.stream().filter(coin::equals).findFirst();
    }

    public List<Coin> findByValue(int value) {
        return Collections.unmodifiableList(coins.stream().filter(c-> c.getValue() == value).toList());
    }

    public Coin first() {
        return coins.stream().findFirst().orElse(null);
    }

    public Queue<Coin> getQueue(Double value) {
        return coins.stream().filter(c->c.getValue() <= value).collect(Collectors.toCollection(LinkedList::new));
    }


}
