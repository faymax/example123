package com.example.demo;

import java.util.Scanner;

public class moneyTransactionToOtherClient {
    private static void moneyTransactionToOtherClient(Scanner sc) {
        System.out.println("Введіть імя: ");
        String nameFrom = sc.nextLine();
        System.out.println("Введіть прізвище: ");
        String surnameFrom = sc.nextLine();
        Query queryFrom = em.createQuery("SELECT c FROM Client c WHERE c.name = :name AND c.surname = :surname", Client.class);
        queryFrom.setParameter("name", nameFrom);
        queryFrom.setParameter("surname", surnameFrom);
        Client clientFrom = (Client) queryFrom.getSingleResult();
        Accounts accFrom = clientFrom.getAccount();
        System.out.println("З якого кошелька зробити перевід?(UAH,USD,EUR) ");
        String money = sc.nextLine();
        System.out.println("Скільки перевести? ");
        Double trans = sc.nextDouble();
        sc.nextLine();
        System.out.println("Введіть імя кому перевести: ");
        String nameTo = sc.nextLine();
        System.out.println("Введіть прізвище кому перевести: ");
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