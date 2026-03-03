package com.Vlad.RepairDesk.controller;

import com.Vlad.RepairDesk.dto.DeviceRequestDTO;
import com.Vlad.RepairDesk.dto.DeviceResponseDTO;
import com.Vlad.RepairDesk.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    // CREATE
    @PostMapping
    public ResponseEntity<DeviceResponseDTO> create(@RequestBody DeviceRequestDTO request) {
        DeviceResponseDTO response = deviceService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<DeviceResponseDTO>> getAll() {
        List<DeviceResponseDTO> devices = deviceService.getAll();
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> getById(@PathVariable Long id) {
        DeviceResponseDTO device = deviceService.getById(id);
        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> update(@PathVariable Long id,
                                                    @RequestBody DeviceRequestDTO request) {
        DeviceResponseDTO updated = deviceService.update(id, request);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}