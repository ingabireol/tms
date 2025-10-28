package com.mun.theatrems.service;

import com.mun.theatrems.model.Booking;
import com.mun.theatrems.model.Payment;
import com.mun.theatrems.model.EPaymentStatus;
import com.mun.theatrems.repository.BookingRepository;
import com.mun.theatrems.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public Payment createPayment(UUID bookingId, Payment payment) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (paymentRepository.findByBookingId(bookingId).isPresent()) {
            throw new RuntimeException("Payment already exists for this booking");
        }

        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        return paymentRepository.save(payment);
    }

    public Payment processPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentStatus(EPaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentByBooking(UUID bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    public Optional<Payment> getPaymentById(UUID id) {
        return paymentRepository.findById(id);
    }

    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByStatus(EPaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    public Payment refundPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentStatus(EPaymentStatus.REFUNDED);
        return paymentRepository.save(payment);
    }
}
