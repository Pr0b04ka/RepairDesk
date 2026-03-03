package com.Vlad.RepairDesk.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}