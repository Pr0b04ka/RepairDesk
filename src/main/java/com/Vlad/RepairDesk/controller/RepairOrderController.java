package com.Vlad.RepairDesk.controller;

import com.Vlad.RepairDesk.dto.RepairOrderRequestDTO;
import com.Vlad.RepairDesk.dto.RepairOrderResponseDTO;
import com.Vlad.RepairDesk.service.RepairOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class RepairOrderController {

    private final RepairOrderService repairOrderService;

    //CREATE
    @PostMapping
    public ResponseEntity<RepairOrderResponseDTO> createOrder(@RequestBody RepairOrderRequestDTO request) {
        RepairOrderResponseDTO response = repairOrderService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //GET ALL
    @GetMapping
    public ResponseEntity<List<RepairOrderResponseDTO>> getAllOrders() {
        List<RepairOrderResponseDTO> orders = repairOrderService.getAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    //GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RepairOrderResponseDTO> getOrderById(@PathVariable Long id) {
        RepairOrderResponseDTO response = repairOrderService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<RepairOrderResponseDTO> updateOrder(
            @PathVariable Long id,
            @RequestBody RepairOrderRequestDTO request
    ) {
        RepairOrderResponseDTO response = repairOrderService.update(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        repairOrderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

