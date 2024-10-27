package br.com.minicurso.rentalcarpro.repositories;

import br.com.minicurso.rentalcarpro.models.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleReposity extends JpaRepository<VehicleModel, UUID> {

    public boolean existsByLicensePlate(String licensePlate);
    public Optional<VehicleModel> findByLicensePlate(String licensePlate);
}
