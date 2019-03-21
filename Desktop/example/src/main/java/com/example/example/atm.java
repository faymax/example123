package com.example.example;

/**
 * Created by User on 19.03.2019.
 */

import java.util.Scanner;
import java.util.Set;


public class atm {


    private Set<CashCard> cashCards;

    public atm(Set<CashCard> cashCards) {
        this.cashCards = cashCards;
    }


    public void setCashCards(Set<CashCard> cashCards) {
        this.cashCards = cashCards;
    }

    public void run() {
        do {
            System.out.println("Вставте картку");
            Scanner inputCardScanner = new Scanner(System.in);


            String inputCard = inputCardScanner.nextLine();

            System.out.println("Введіть імя");
            String lastName = inputCardScanner.nextLine();

            System.out.println("Введіть пін");
            int pinCode = inputCardScanner.nextInt();

            CashCard cashCardToCheck = new CashCard(lastName, pinCode, 0);

            if (!cashCards.contains(cashCardToCheck)) {
                System.out.println("Неправильно введені імя або пін!");

                continue;
            } else {
                do {
                    System.out.println("Введіть суму для операції або 0 для виходу");
                    int moneyAmount = inputCardScanner.nextInt();
                    if (moneyAmount == 0)
                        break;
                    try {
                        for (CashCard cashCard : cashCards) {
                            if (cashCard.equals(cashCardToCheck)) {
                                System.out.println("Сума " +
                                        cashCard.takeMoney(moneyAmount));

                                System.out.println("На вашому рахунку залишилось "
                                        + cashCard.getBalance());
                                break;
                            }
                        }
                    } catch (NotEnoughMoneyException e) {
                        System.out.println("Недостатньо коштів для зняття");
                    }
                } while (true);
            }
        } while (true);
    }

}
