package com.mun.theatrems.model;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_number", unique = true, nullable = false)
    private String bookingNumber;

    @Column(name = "number_of_seats")
    private Integer numberOfSeats;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private EBookingStatus bookingStatus = EBookingStatus.PENDING;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(name = "seat_numbers")
    private String seatNumbers;

    // MANY-TO-ONE relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    // MANY-TO-ONE relationship with Screening
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_id")
    @ToString.Exclude
    private Screening screening;

    // ONE-TO-ONE relationship with Payment
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        bookingDate = LocalDateTime.now();
        if (bookingNumber == null) {
            bookingNumber = generateBookingNumber();
        }
    }

    private String generateBookingNumber() {
        return "BK" + System.currentTimeMillis();
    }
}
