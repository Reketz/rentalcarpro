package br.com.minicurso.rentalcarpro.services;

import br.com.minicurso.rentalcarpro.models.VehicleModel;
import br.com.minicurso.rentalcarpro.repositories.VehicleReposity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleReposity vehicleReposity;

    public VehicleService(VehicleReposity vehicleReposity) {
        this.vehicleReposity = vehicleReposity;
    }

    public List<VehicleModel> getAll() {
        return this.vehicleReposity.findAll();
    }

    public Optional<VehicleModel> getOne(UUID id) {
        return this.vehicleReposity.findById(id);
    }

    public Optional<VehicleModel> getOneByLicensePlate(String licensePlate) {
        return this.vehicleReposity.findByLicensePlate(licensePlate);
    }

    @Transactional
    public VehicleModel save(VehicleModel vehicleModel) {
        return this.vehicleReposity.save(vehicleModel);
    }

    @Transactional
    public void delete(VehicleModel vehicleModel) {
        this.vehicleReposity.delete(vehicleModel);
    }

    public boolean existsByLicensePlate(String licensePlate) {
        return this.vehicleReposity.existsByLicensePlate(licensePlate);
    }
}
