package br.com.minicurso.rentalcarpro.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class RentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private VehicleModel vehicle;

    @Column(nullable = false)
    private LocalDateTime rentalDate;

    private LocalDateTime returnDate;
    private double totalPrice;
}
