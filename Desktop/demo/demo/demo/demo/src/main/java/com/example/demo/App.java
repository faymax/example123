package com.example.demo;

import com.example.demo.entity.Accounts;
import com.example.demo.entity.Client;

import javax.persistence.*;
import java.util.*;

public class App {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            emf = Persistence.createEntityManagerFactory("JPADB");
            em = emf.createEntityManager();

            try {
                while (true) {
                    System.out.println("1: Відкрити рахунок");
                    System.out.println("2: Покласти/зняти");
                    System.out.println("3: Провірити баланс");
                    System.out.println("4: Перевести гроші іншому");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            openAccount(sc);
                            break;
                        case "2":
                            DepositOrWithdrawAccount(sc);
                            break;
                        case "3":
                            accountBalance(sc);
                            break;
                        case "4":
                            moneyTransactionToOtherClient(sc);
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void openAccount(Scanner sc) {
        System.out.println("Введіть своє імя: ");
        String name = sc.nextLine();
        System.out.println("Введіть своє прізвище: ");
        String surname = sc.nextLine();
        em.getTransaction().begin();
        try {
            Client client = new Client(name, surname);
            Accounts acc = new Accounts();
            acc.setUAH(0);
            acc.setEUR(0);
            acc.setUSD(0);
            em.persist(acc);
            client.setAccount(acc);
            em.persist(client);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void DepositOrWithdrawAccount(Scanner sc) {
        System.out.println("Введіть імя клієнта: ");
        String name = sc.nextLine();
        System.out.println("Введіть прізвище клієнта: ");
        String surname = sc.nextLine();
        System.out.println("Гроші потрібно покласти чи зняти?(deposit/withdraw)");
        String depOrwith = sc.nextLine();
        String currency = null;
        Double sum = 0.0;
        if (depOrwith.equalsIgnoreCase("deposit")) {
            System.out.println("Виберіть валюту?(UAH,EUR,USD) ");
            currency = sc.nextLine();
            System.out.println("Скільки покласти на рахунок? ");
            sum = sc.nextDouble();
        } else if (depOrwith.equalsIgnoreCase("withdraw")) {
            System.out.println("В якій валюті зняти гроші?(UAH,EUR,USD) ");
            currency = sc.nextLine();
            System.out.println("Скільки зняти? ");
            sum = sc.nextDouble();
        }

        Query query = em.createQuery("SELECT c FROM Client c WHERE c.name = :name AND c.surname = :surname", Client.class);
        query.setParameter("name", name);
        query.setParameter("surname", surname);
        Client client = (Client) query.getSingleResult();
        Accounts acc = client.getAccount();
        if (depOrwith.equalsIgnoreCase("deposit")) {
            if (currency.equalsIgnoreCase("UAH")) {
                acc.setUAH(acc.getUAH() + sum);
            } else if (currency.equalsIgnoreCase("EUR")) {
                acc.setEUR(acc.getEUR() + sum);
            } else if (currency.equalsIgnoreCase("USD")) {
                acc.setUSD(acc.getUSD() + sum);
            }
        } else if (depOrwith.equalsIgnoreCase("withdraw")) {
            if (currency.equalsIgnoreCase("UAH")) {
                if (acc.getUAH() >= sum) {
                    acc.setUAH(acc.getUAH() - sum);
                }
            } else if (currency.equalsIgnoreCase("EUR")) {
                if (acc.getEUR() >= sum) {
                    acc.setEUR(acc.getEUR() - sum);
                }
            } else if (currency.equalsIgnoreCase("USD")) {
                if (acc.getUSD() >= sum) {
                    acc.setUSD(acc.getUSD() - sum);
                }
            }
        }
        em.getTransaction().begin();
        try {
            em.merge(acc);

            Transaction transaction = new Transaction();
            transaction.setAccount(acc);
            transaction.setCurrency(null);

            if (sum > 0) {
                transaction.setCurrency(currency);
                if (depOrwith.equalsIgnoreCase("deposit")) {
                    transaction.setTransactionName("DEPOSIT: " + sum);
                } else if (depOrwith.equalsIgnoreCase("withdraw")) {
                    transaction.setTransactionName("WITHDRAW: " + sum);
                }
                em.persist(transaction);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }

    }

    private static void accountBalance(Scanner sc) {
        double courseUSD = 27.38;
        double courseEUR = 31.29;
        System.out.println("Введіть всоє імя: ");
        String name = sc.nextLine();
        System.out.println("Введіть своє прізвище: ");
        String surname = sc.nextLine();
        Query query = em.createQuery("SELECT c FROM Client c WHERE c.name = :name AND c.surname = :surname", Client.class);
        query.setParameter("name", name);
        query.setParameter("surname", surname);
        Client client = (Client) query.getSingleResult();
        Accounts acc = client.getAccount();
        double usdInUAH = acc.getUSD() * courseUSD;
        double eurInUAH = acc.getEUR() * courseEUR;
        System.out.println("Вітаю " + name + "! Ваш баланс: \n"
                + "USD : " + acc.getUSD() + " или " + usdInUAH + " UAH\n"
                + "EUR : " + acc.getEUR() + " или " + eurInUAH + " UAH\n"
                + "UAH : " + acc.getUAH());

        System.out.println("Сума в грн. = " + (acc.getUAH() + usdInUAH + eurInUAH) + " UAH");
    }

    private static void moneyTransactionToOtherClient(Scanner sc) {
        System.out.println("Введіть своє імя: ");
        String nameFrom = sc.nextLine();
        System.out.println("Введіть своє прізвище: ");
        String surnameFrom = sc.nextLine();
        Query queryFrom = em.createQuery("SELECT c FROM Client c WHERE c.name = :name AND c.surname = :surname", Client.class);
        queryFrom.setParameter("name", nameFrom);
        queryFrom.setParameter("surname", surnameFrom);
        Client clientFrom = (Client) queryFrom.getSingleResult();
        Accounts accFrom = clientFrom.getAccount();
        System.out.println("З якого кошелька перевести?(UAH,USD,EUR) ");
        String money = sc.nextLine();
        System.out.println("Скільки перевести? ");
        Double trans = sc.nextDouble();
        sc.nextLine();
        System.out.println("Введіть імя отримувача: ");
        String nameTo = sc.nextLine();
        System.out.println("Введіть прізвище отримувача: ");
        String surnameTo = sc.nextLine();
        Query queryTo = em.createQuery("SELECT c FROM Client c WHERE c.name = :name AND c.surname = :surname", Client.class);
        queryTo.setParameter("name", nameTo);
        queryTo.setParameter("surname", surnameTo);
        Client clientTo = (Client) queryFrom.getSingleResult();
        Accounts accTo = clientTo.getAccount();
        if (money.equalsIgnoreCase("UAH")) {
            if (accFrom.getUAH() >= trans) {
                accFrom.setUAH(accFrom.getUAH() - trans);
                accTo.setUAH(accTo.getUAH() + trans);
            }
        } else if (money.equalsIgnoreCase("USD")) {
            if (accFrom.getUSD() >= trans) {
                accFrom.setUSD(accFrom.getUSD() - trans);
                accTo.setUSD(accTo.getUSD() + trans);
            }
        } else if (money.equalsIgnoreCase("EUR")) {
            if (accFrom.getEUR() >= trans) {
                accFrom.setEUR(accFrom.getEUR() - trans);
                accTo.setEUR(accTo.getEUR() + trans);
            }
        }

        em.getTransaction().begin();
        try {
            em.merge(accFrom);
            Transaction transaction = new Transaction();
            transaction.setAccount(accFrom);
            transaction.setCurrency(null);
            if (trans > 0) {
                transaction.setCurrency(money);
                transaction.setTransactionName("WITHDRAW: " + trans);
                em.persist(transaction);
            }
            em.merge(accTo);
            transaction.setAccount(accTo);
            transaction.setCurrency(null);
            if (trans > 0) {
                transaction.setCurrency(money);
                transaction.setTransactionName("DEPOSIT: " + trans);
                em.persist(transaction);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }

    }
}

