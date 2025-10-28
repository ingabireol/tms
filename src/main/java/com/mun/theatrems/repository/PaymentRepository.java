package com.mun.theatrems.repository;

import com.mun.theatrems.model.Payment;
import com.mun.theatrems.model.EPaymentStatus;
import com.mun.theatrems.model.EPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByBookingId(UUID bookingId);

    List<Payment> findByPaymentStatus(EPaymentStatus status);

    List<Payment> findByPaymentMethod(EPaymentMethod method);
}
