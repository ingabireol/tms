package com.mun.theatrems.model;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "screenings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "screening_date", nullable = false)
    private LocalDate screeningDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "screening_type")
    private EScreeningType screeningType = EScreeningType.REGULAR;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // MANY-TO-ONE relationship with Movie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    @ToString.Exclude
    private Movie movie;

    // MANY-TO-ONE relationship with Theatre
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id", nullable = false)
    @ToString.Exclude
    private Theatre theatre;

    // MANY-TO-ONE relationship with Hall
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    @ToString.Exclude
    private Hall hall;
}
