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
        order.setStatus(RepairOrder.Status.NEW);
        getFinalPrice(order);

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

    public RepairOrderRequestDTO getByIdForEdit(Long id) {

        RepairOrder order = repairOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        RepairOrderRequestDTO dto = new RepairOrderRequestDTO();

        dto.setId(order.getId());

        dto.setClientId(order.getClient().getId());
        dto.setDeviceId(order.getDevice().getId());

        dto.setComplaint(order.getComplaint());
        dto.setDiagnosticsResult(order.getDiagnosticsResult());
        dto.setRepairApproved(order.getRepairApproved());
        dto.setPartsCost(order.getPartsCost());
        dto.setLaborCost(order.getLaborCost());
        dto.setFinalSummary(order.getFinalSummary());
        dto.setTimeSpentHours(order.getTimeSpentHours());
        dto.setStatus(order.getStatus());

        return dto;
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
        getFinalPrice(order);
        order.setStatus(determineStatus(order));

        RepairOrder updated = repairOrderRepository.save(order);

        return mapToResponseDTO(updated);
    }

    //DELETE
    public void delete(Long id) {
        repairOrderRepository.deleteById(id);
    }

    private RepairOrder.Status determineStatus(RepairOrder order) {

        if (!order.getFinalSummary().isBlank() && order.getTimeSpentHours() != null && order.getLaborCost() != null && order.getPartsCost() != null && order.getRepairApproved() && order.getDiagnosticsResult() != null) {
            return RepairOrder.Status.COMPLETED;
        }

        if (order.getPartsCost() != null && order.getRepairApproved() && order.getDiagnosticsResult() != null) {
            return RepairOrder.Status.REPAIRING;
        }

        if (order.getRepairApproved() && order.getDiagnosticsResult() != null) {
            return RepairOrder.Status.WAITING_PARTS;
        }

        if (order.getDiagnosticsResult() != null && !order.getDiagnosticsResult().isBlank() && !order.getRepairApproved()) {
            return RepairOrder.Status.CANCELED;
        }

        return RepairOrder.Status.DIAGNOSING;
    }

    private void getFinalPrice(RepairOrder order) {
        double parts = order.getPartsCost() != null ? order.getPartsCost() : 0;
        double labor = order.getLaborCost() != null ? order.getLaborCost() : 0;
        order.setFinalPrice(parts + labor);
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
        dto.setFinalSummary(order.getFinalSummary());
        dto.setTimeSpentHours(order.getTimeSpentHours());
        dto.setStatus(order.getStatus());
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