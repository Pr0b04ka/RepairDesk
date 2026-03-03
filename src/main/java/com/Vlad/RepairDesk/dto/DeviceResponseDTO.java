package com.Vlad.RepairDesk.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeviceResponseDTO {
    private Long id;
    private String brand;
    private String model;
    private String type;
}