package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.repo.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 1 on 21.03.2019.
 */
@Service
public class ClientService implements ClientServiceImpl {

    @Autowired
    private ClientRepo clientRepo;

    @Override
    public Client findById(Long id) {
        return clientRepo.getOne(id);
    }
}
