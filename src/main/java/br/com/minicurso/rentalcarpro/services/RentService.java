package br.com.minicurso.rentalcarpro.services;

import br.com.minicurso.rentalcarpro.exceptions.RentException;
import br.com.minicurso.rentalcarpro.exceptions.VehicleException;
import br.com.minicurso.rentalcarpro.models.RentModel;
import br.com.minicurso.rentalcarpro.models.VehicleModel;
import br.com.minicurso.rentalcarpro.repositories.RentRepository;
import br.com.minicurso.rentalcarpro.repositories.VehicleReposity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RentService {

    private final RentRepository rentRepository;
    private final VehicleReposity vehicleReposity;

    public RentService(RentRepository rentRepository, VehicleReposity vehicleReposity) {
        this.rentRepository = rentRepository;
        this.vehicleReposity = vehicleReposity;
    }

    public RentModel rent(String licensePlate) throws VehicleException {
        Optional<VehicleModel> vehicleModelOptional = vehicleReposity.findByLicensePlate(licensePlate);
        if(!vehicleModelOptional.isPresent()) {
            throw new VehicleException("Vehicle not found by license plate");
        }

        VehicleModel vehicleModel = vehicleModelOptional.get();
        if(vehicleModel.isRented()) {
            throw new VehicleException("Vehicle is not available for rent.");
        }

        vehicleModel.setRented(true);
        vehicleReposity.save(vehicleModel);

        RentModel rentModel = new RentModel();
        rentModel.setVehicle(vehicleModel);
        rentModel.setRentalDate(LocalDateTime.now());
        return this.rentRepository.save(rentModel);
    }

    public RentModel returnVehicle(String licensePlate) throws VehicleException, RentException {
        Optional<VehicleModel> vehicleModelOptional = vehicleReposity.findByLicensePlate(licensePlate);
        if(!vehicleModelOptional.isPresent()) {
            throw new VehicleException("Vehicle not found by license plate");
        }

        VehicleModel vehicleModel = vehicleModelOptional.get();
        if(!vehicleModel.isRented()) {
            throw new VehicleException("Vehicle already returned.");
        }

        Optional<RentModel> vehicleRent = rentRepository.findByVehicleAndReturnDateIsNull(vehicleModel);
        if(!vehicleRent.isPresent()) {
            throw new RentException("Vehicle not found or already returned.");
        }

        RentModel rentModel = vehicleRent.get();
        rentModel.setReturnDate(LocalDateTime.now());

        long daysRented = Duration.between(rentModel.getRentalDate(), rentModel.getReturnDate()).toDays() + 1;
        double total = vehicleModel.getRentalPricePerDay() * daysRented;
        rentModel.setTotalPrice(total);

        vehicleModel.setRented(false);
        vehicleReposity.save(vehicleModel);

        return rentRepository.save(rentModel);
    }
}
