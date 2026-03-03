package com.Vlad.RepairDesk.service;

import com.Vlad.RepairDesk.dto.ClientRequestDTO;
import com.Vlad.RepairDesk.dto.ClientResponseDTO;
import com.Vlad.RepairDesk.model.Client;
import com.Vlad.RepairDesk.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    // CREATE
    public ClientResponseDTO create(ClientRequestDTO request) {
        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhone(request.getPhone());
        client.setEmail(request.getEmail());

        Client saved = clientRepository.save(client);
        return mapToResponseDTO(saved);
    }

    // GET ALL
    public List<ClientResponseDTO> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public ClientResponseDTO getById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return mapToResponseDTO(client);
    }

    // UPDATE
    public ClientResponseDTO update(Long id, ClientRequestDTO request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhone(request.getPhone());
        client.setEmail(request.getEmail());

        Client updated = clientRepository.save(client);
        return mapToResponseDTO(updated);
    }

    // DELETE
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    // Маппинг
    private ClientResponseDTO mapToResponseDTO(Client client) {
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setPhone(client.getPhone());
        dto.setEmail(client.getEmail());
        return dto;
    }
}