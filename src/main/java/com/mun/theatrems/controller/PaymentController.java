package com.mun.theatrems.controller;

import com.mun.theatrems.model.Payment;
import com.mun.theatrems.model.EPaymentStatus;
import com.mun.theatrems.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/booking/{bookingId}")
    public ResponseEntity<?> createPayment(
            @PathVariable UUID bookingId,
            @RequestBody Payment payment) {
        try {
            Payment createdPayment = paymentService.createPayment(bookingId, payment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable UUID id) {
        return paymentService.getPaymentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getPaymentByBooking(@PathVariable UUID bookingId) {
        return paymentService.getPaymentByBooking(bookingId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<?> getPaymentByTransactionId(@PathVariable String transactionId) {
        return paymentService.getPaymentByTransactionId(transactionId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable EPaymentStatus status) {
        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<Payment> processPayment(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(paymentService.processPayment(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(paymentService.refundPayment(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
