package com.Vlad.RepairDesk.service;

import com.Vlad.RepairDesk.dto.DeviceRequestDTO;
import com.Vlad.RepairDesk.dto.DeviceResponseDTO;
import com.Vlad.RepairDesk.model.Device;
import com.Vlad.RepairDesk.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    // CREATE
    public DeviceResponseDTO create(DeviceRequestDTO request) {
        Device device = new Device();
        device.setBrand(request.getBrand());
        device.setModel(request.getModel());
        device.setType(request.getType());

        Device saved = deviceRepository.save(device);
        return mapToResponseDTO(saved);
    }

    // GET ALL
    public List<DeviceResponseDTO> getAll() {
        return deviceRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public DeviceResponseDTO getById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));
        return mapToResponseDTO(device);
    }

    // UPDATE
    public DeviceResponseDTO update(Long id, DeviceRequestDTO request) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        device.setBrand(request.getBrand());
        device.setModel(request.getModel());
        device.setType(request.getType());

        Device updated = deviceRepository.save(device);
        return mapToResponseDTO(updated);
    }

    // DELETE
    public void delete(Long id) {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        if (device.getRepairOrders() != null && !device.getRepairOrders().isEmpty()) {
            throw new IllegalStateException("Нельзя удалить устройство: есть связанные заказы");
        }

        deviceRepository.delete(device);
    }

    // Mapping
    private DeviceResponseDTO mapToResponseDTO(Device device) {
        DeviceResponseDTO dto = new DeviceResponseDTO();
        dto.setId(device.getId());
        dto.setBrand(device.getBrand());
        dto.setModel(device.getModel());
        dto.setType(device.getType());
        return dto;
    }
}