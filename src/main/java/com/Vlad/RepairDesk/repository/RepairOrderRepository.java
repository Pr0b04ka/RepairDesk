package com.Vlad.RepairDesk.repository;

import com.Vlad.RepairDesk.model.RepairOrder;
import com.Vlad.RepairDesk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, Long> {

    List<RepairOrder> findByUser(User user);

}