package com.Vlad.RepairDesk.dto;

import com.Vlad.RepairDesk.model.RepairOrder;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepairOrderRequestDTO {
    private Long id;
    private String complaint;
    private String diagnosticsResult;
    private Boolean repairApproved;
    private Double partsCost;
    private Double laborCost;
    private Double finalPrice;
    private String finalSummary;
    private Double timeSpentHours;
    private RepairOrder.Status status;
    private Long clientId;
    private Long deviceId;
}