package com.example.demo.repo;

import com.example.demo.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 1 on 21.03.2019.
 */
public interface ClientRepo extends JpaRepository<Client,Long> {


}
