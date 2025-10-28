package com.mun.theatrems.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "row_number")
    private String rowNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type")
    private ESeatType seatType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // MANY-TO-ONE relationship with Hall
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    @ToString.Exclude
    private Hall hall;
}
