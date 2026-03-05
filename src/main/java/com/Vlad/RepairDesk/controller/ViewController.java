package com.Vlad.RepairDesk.controller;

import com.Vlad.RepairDesk.dto.ClientRequestDTO;
import com.Vlad.RepairDesk.dto.DeviceRequestDTO;
import com.Vlad.RepairDesk.dto.RepairOrderRequestDTO;
import com.Vlad.RepairDesk.dto.RepairOrderResponseDTO;
import com.Vlad.RepairDesk.model.RepairOrder;
import com.Vlad.RepairDesk.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final RepairOrderService repairOrderService;
    private final ClientService clientService;
    private final DeviceService deviceService;
    private final ExchangeRateService exchangeRateService;
    private final AksParserService aksParserService;

    @GetMapping("/")
    public String landing() { return "index"; }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<RepairOrderResponseDTO> all = repairOrderService.getAll();
        Map<String, BigDecimal> rates = exchangeRateService.getRates();

        model.addAttribute("totalOrders", all.size());
        model.addAttribute("activeOrders", all.stream()
                .filter(o -> o.getStatus() != RepairOrder.Status.COMPLETED
                        && o.getStatus() != RepairOrder.Status.CANCELED).count());
        model.addAttribute("completedOrders", all.stream()
                .filter(o -> o.getStatus() == RepairOrder.Status.COMPLETED).count());
        model.addAttribute("totalClients", clientService.getAll().size());
        model.addAttribute("totalDevices", deviceService.getAll().size());
        model.addAttribute("recentOrders", all.stream().limit(10).collect(Collectors.toList()));
        model.addAttribute("usdRate", rates.getOrDefault("USD", BigDecimal.valueOf(44)));
        model.addAttribute("eurRate", rates.getOrDefault("EUR", BigDecimal.valueOf(51)));
        return "dashboard";
    }

    @GetMapping("/parts/search")
    public String searchParts(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("activePage", "orders");
        model.addAttribute("query", query);
        if (query != null && !query.isBlank()) {
            model.addAttribute("results", aksParserService.search(query));
        }
        return "parts_search";
    }

    // ------------------ ORDERS ------------------

    @GetMapping("/orders")
    public String listOrders(Model model) {
        Map<String, BigDecimal> rates = exchangeRateService.getRates();
        model.addAttribute("orders", repairOrderService.getAll());
        model.addAttribute("usdRate", rates.getOrDefault("USD", BigDecimal.valueOf(44)));
        model.addAttribute("eurRate", rates.getOrDefault("EUR", BigDecimal.valueOf(51)));
        return "orders";
    }

    @GetMapping("/orders/create")
    public String createOrderForm(Model model) {
        model.addAttribute("order", new RepairOrderRequestDTO());
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("devices", deviceService.getAll());
        return "create_order";
    }

    @PostMapping("/orders/create")
    public String createOrderSubmit(@ModelAttribute("order") RepairOrderRequestDTO order) {
        repairOrderService.create(order);
        return "redirect:/orders";
    }

    @GetMapping("/orders/edit/{id}")
    public String editOrderForm(@PathVariable Long id, Model model) {
        model.addAttribute("order", repairOrderService.getByIdForEdit(id));
        model.addAttribute("orderId", id);
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("devices", deviceService.getAll());
        return "edit_order";
    }

    @PostMapping("/orders/edit/{id}")
    public String editOrderSubmit(@PathVariable Long id,
                                  @ModelAttribute("order") RepairOrderRequestDTO order) {
        repairOrderService.update(id, order);
        return "redirect:/orders";
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        repairOrderService.delete(id);
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

    @GetMapping("/clients/edit/{id}")
    public String editClientForm(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.getById(id));
        return "edit_client";
    }

    @PostMapping("/clients/edit/{id}")
    public String editClientSubmit(@PathVariable Long id,
                                   @ModelAttribute("client") ClientRequestDTO client) {
        clientService.update(id, client);
        return "redirect:/clients";
    }

    @GetMapping("/clients/delete/{id}")
    public String deleteClient(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {

        try {
            clientService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Клиент успешно удалён");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    ex.getMessage());
        }

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

    @GetMapping("/devices/edit/{id}")
    public String editDeviceForm(@PathVariable Long id, Model model) {
        model.addAttribute("device", deviceService.getById(id));
        return "edit_device";
    }

    @PostMapping("/devices/edit/{id}")
    public String editDeviceSubmit(@PathVariable Long id,
                                   @ModelAttribute("device") DeviceRequestDTO device) {
        deviceService.update(id, device);
        return "redirect:/devices";
    }

    @GetMapping("/devices/delete/{id}")
    public String deleteDevice(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {

        try {
            deviceService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Устройство успешно удалено");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    ex.getMessage());
        }

        return "redirect:/devices";
    }
}