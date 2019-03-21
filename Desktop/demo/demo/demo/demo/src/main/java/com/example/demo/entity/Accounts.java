package com.example.demo.entity;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "USD", nullable = false)
    private double USD;

    @Column(name = "EUR", nullable = false)
    private double EUR;

    @Column(name = "UAH", nullable = false)
    private double UAH;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account")
    private Client client;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();


    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEUR(double EUR) {
        this.EUR = EUR;
    }

    public double getEUR() {
        return EUR;
    }

    public void setUAH(double UAH) {
        this.UAH = UAH;
    }

    public double getUAH() {
        return UAH;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }

    public double getUSD() {
        return USD;
    }

}