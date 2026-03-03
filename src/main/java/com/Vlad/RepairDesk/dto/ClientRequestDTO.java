package com.Vlad.RepairDesk.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientRequestDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}