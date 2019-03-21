package com.example.example;

import java.util.*;

public class Test {

    public static void main(String[] args) {
        CashCard card1 = new CashCard("Петро", 1111, 9000);
        CashCard card2 = new CashCard("Павло", 1234, 100);
        CashCard card3 = new CashCard("Іра", 6969, 1000000);

        Set<CashCard> cashCards = new HashSet<CashCard>();
        cashCards.add(card1);
        cashCards.add(card2);
        cashCards.add(card3);

        new atm(cashCards).run();
    }
}