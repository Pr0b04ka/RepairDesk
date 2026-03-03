package com.Vlad.RepairDesk.controller;

import com.Vlad.RepairDesk.dto.ClientRequestDTO;
import com.Vlad.RepairDesk.dto.DeviceRequestDTO;
import com.Vlad.RepairDesk.dto.RepairOrderRequestDTO;
import com.Vlad.RepairDesk.repository.ClientRepository;
import com.Vlad.RepairDesk.repository.DeviceRepository;
import com.Vlad.RepairDesk.service.ClientService;
import com.Vlad.RepairDesk.service.DeviceService;
import com.Vlad.RepairDesk.service.RepairOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final RepairOrderService repairOrderService;
    private final ClientService clientService;
    private final DeviceService deviceService;

    private final ClientRepository clientRepository;
    private final DeviceRepository deviceRepository;

    // ------------------ ORDERS ------------------

    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", repairOrderService.getAll());
        return "orders";
    }

    @GetMapping("/orders/create")
    public String createOrderForm(Model model) {
        model.addAttribute("order", new RepairOrderRequestDTO());
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        return "create_order";
    }

    @PostMapping("/orders/create")
    public String createOrderSubmit(@ModelAttribute("order") RepairOrderRequestDTO order) {
        repairOrderService.create(order);
        return "redirect:/orders";
    }

    // ------------------ CLIENTS ------------------

    @GetMapping("/clients")
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.getAll());
        return "clients";
    }

    @GetMapping("/clients/create")
    public String createClientForm(Model model) {
        model.addAttribute("client", new ClientRequestDTO());
        return "create_client";
    }

    @PostMapping("/clients/create")
    public String createClientSubmit(@ModelAttribute("client") ClientRequestDTO client) {
        clientService.create(client);
        return "redirect:/clients";
    }

    // ------------------ DEVICES ------------------

    @GetMapping("/devices")
    public String listDevices(Model model) {
        model.addAttribute("devices", deviceService.getAll());
        return "devices";
    }

    @GetMapping("/devices/create")
    public String createDeviceForm(Model model) {
        model.addAttribute("device", new DeviceRequestDTO());
        return "create_device";
    }

    @PostMapping("/devices/create")
    public String createDeviceSubmit(@ModelAttribute("device") DeviceRequestDTO device) {
        deviceService.create(device);
        return "redirect:/devices";
    }
}