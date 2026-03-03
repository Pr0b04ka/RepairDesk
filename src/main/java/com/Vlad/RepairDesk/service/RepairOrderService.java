package com.Vlad.RepairDesk.service;

import com.Vlad.RepairDesk.dto.RepairOrderRequestDTO;
import com.Vlad.RepairDesk.dto.RepairOrderResponseDTO;
import com.Vlad.RepairDesk.model.Client;
import com.Vlad.RepairDesk.model.Device;
import com.Vlad.RepairDesk.model.RepairOrder;
import com.Vlad.RepairDesk.repository.ClientRepository;
import com.Vlad.RepairDesk.repository.DeviceRepository;
import com.Vlad.RepairDesk.repository.RepairOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepairOrderService {

    private final RepairOrderRepository repairOrderRepository;
    private final ClientRepository clientRepository;
    private final DeviceRepository deviceRepository;

    //CREATE
    public RepairOrderResponseDTO create(RepairOrderRequestDTO request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        RepairOrder order = new RepairOrder();
        order.setComplaint(request.getComplaint());
        order.setDiagnosticsResult(request.getDiagnosticsResult());
        order.setRepairApproved(request.getRepairApproved());
        order.setPartsCost(request.getPartsCost());
        order.setLaborCost(request.getLaborCost());
        order.setFinalSummary(request.getFinalSummary());
        order.setTimeSpentHours(request.getTimeSpentHours());
        order.setCreatedAt(LocalDateTime.now());
        order.setClient(client);
        order.setDevice(device);
        applyBusinessLogic(order);

        RepairOrder saved = repairOrderRepository.save(order);

        return mapToResponseDTO(saved);
    }

    //GET ALL
    public List<RepairOrderResponseDTO> getAll() {
        return repairOrderRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //GET BY ID
    public RepairOrderResponseDTO getById(Long id) {
        RepairOrder order = repairOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RepairOrder not found"));
        return mapToResponseDTO(order);
    }

    //UPDATE
    public RepairOrderResponseDTO update(Long id, RepairOrderRequestDTO request) {
        RepairOrder order = repairOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RepairOrder not found"));

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        order.setComplaint(request.getComplaint());
        order.setDiagnosticsResult(request.getDiagnosticsResult());
        order.setRepairApproved(request.getRepairApproved());
        order.setPartsCost(request.getPartsCost());
        order.setLaborCost(request.getLaborCost());
        order.setFinalSummary(request.getFinalSummary());
        order.setTimeSpentHours(request.getTimeSpentHours());
        order.setClient(client);
        order.setDevice(device);
        applyBusinessLogic(order);

        RepairOrder updated = repairOrderRepository.save(order);

        return mapToResponseDTO(updated);
    }

    //DELETE
    public void delete(Long id) {
        repairOrderRepository.deleteById(id);
    }

    private void applyBusinessLogic(RepairOrder order) {
        if (Boolean.FALSE.equals(order.getRepairApproved())) {
            order.setFinalPrice(0.0);
            order.setStatus(RepairOrder.Status.CANCELED);
        } else {
            // finalPrice = partsCost + laborCost
            double parts = order.getPartsCost() != null ? order.getPartsCost() : 0;
            double labor = order.getLaborCost() != null ? order.getLaborCost() : 0;
            order.setFinalPrice(parts + labor);

            // если статус ещё не задан, по умолчанию NEW
            if (order.getStatus() == null) {
                order.setStatus(RepairOrder.Status.NEW);
            }
        }
    }

    //Entity -> ResponseDTO
    private RepairOrderResponseDTO mapToResponseDTO(RepairOrder order) {
        RepairOrderResponseDTO dto = new RepairOrderResponseDTO();
        dto.setId(order.getId());
        dto.setComplaint(order.getComplaint());
        dto.setDiagnosticsResult(order.getDiagnosticsResult());
        dto.setRepairApproved(order.getRepairApproved());
        dto.setPartsCost(order.getPartsCost());
        dto.setLaborCost(order.getLaborCost());
        dto.setFinalPrice(order.getFinalPrice());
        dto.setCreatedAt(order.getCreatedAt());

        //client data
        dto.setClientFirstName(order.getClient().getFirstName());
        dto.setClientLastName(order.getClient().getLastName());
        dto.setClientPhone(order.getClient().getPhone());

        //device data
        dto.setDeviceBrand(order.getDevice().getBrand());
        dto.setDeviceModel(order.getDevice().getModel());
        dto.setDeviceType(order.getDevice().getType());

        return dto;
    }
}