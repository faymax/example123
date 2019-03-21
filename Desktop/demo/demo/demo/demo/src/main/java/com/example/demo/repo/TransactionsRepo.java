package com.example.demo.repo;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 1 on 21.03.2019.
 */
public interface TransactionsRepo extends JpaRepository<Transaction,Long> {

}
