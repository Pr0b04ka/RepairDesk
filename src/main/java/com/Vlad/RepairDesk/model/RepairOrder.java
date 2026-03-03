package com.Vlad.RepairDesk.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "repair_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepairOrder {

    public enum Status {
        NEW,
        DIAGNOSING,
        WAITING_PARTS,
        REPAIRING,
        COMPLETED,
        CANCELED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String complaint;

    private String diagnosticsResult;

    private Boolean repairApproved;

    private Double partsCost;

    private Double laborCost;

    private Double finalPrice;

    private String finalSummary;

    private Double timeSpentHours;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
}