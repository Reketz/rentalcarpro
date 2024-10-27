package br.com.minicurso.rentalcarpro.repositories;

import br.com.minicurso.rentalcarpro.models.RentModel;
import br.com.minicurso.rentalcarpro.models.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RentRepository extends JpaRepository<RentModel, UUID> {
    public Optional<RentModel> findByVehicleAndReturnDateIsNull(VehicleModel vehicleModel);
}
