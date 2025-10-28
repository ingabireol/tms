package com.mun.theatrems.model;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    private UUID id;

    // ONE-TO-ONE relationship with Booking (owner side)
    @OneToOne
    @MapsId
    @JoinColumn(name = "booking_id")
    @ToString.Exclude
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private EPaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private EPaymentStatus paymentStatus = EPaymentStatus.PENDING;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "card_last_4_digits")
    private String cardLast4Digits;

    @Column(name = "mobile_money_number")
    private String mobileMoneyNumber;

    @PrePersist
    protected void onCreate() {
        if (transactionId == null) {
            transactionId = "TXN" + System.currentTimeMillis();
        }
    }
}
