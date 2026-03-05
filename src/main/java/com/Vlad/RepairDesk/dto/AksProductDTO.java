package com.Vlad.RepairDesk.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class AksProductDTO {
    private String name;
    private String price;
    private String oldPrice;
    private String url;
    private boolean inStock;
}