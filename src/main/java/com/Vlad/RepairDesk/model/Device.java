package com.Vlad.RepairDesk.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "repairOrders")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String type;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<RepairOrder> repairOrders;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}