package com.Vlad.RepairDesk.repository;

import com.Vlad.RepairDesk.model.Client;
import com.Vlad.RepairDesk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByUser(User user);

}