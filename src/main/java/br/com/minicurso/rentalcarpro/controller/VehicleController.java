package br.com.minicurso.rentalcarpro.controller;

import br.com.minicurso.rentalcarpro.dtos.VehicleDTO;
import br.com.minicurso.rentalcarpro.exceptions.RentException;
import br.com.minicurso.rentalcarpro.exceptions.VehicleException;
import br.com.minicurso.rentalcarpro.models.RentModel;
import br.com.minicurso.rentalcarpro.models.VehicleModel;
import br.com.minicurso.rentalcarpro.services.RentService;
import br.com.minicurso.rentalcarpro.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final RentService rentService;

    public VehicleController(VehicleService vehicleService, RentService rentService) {
        this.vehicleService = vehicleService;
        this.rentService = rentService;
    }

    @PostMapping()
    public ResponseEntity<Object> createVehicle(@RequestBody @Valid VehicleDTO vehicleDTO) {
        if(vehicleService.existsByLicensePlate(vehicleDTO.getLicensePlate())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("License plate exists!!!");
        }

        VehicleModel vehicleModel = new VehicleModel();
        BeanUtils.copyProperties(vehicleDTO, vehicleModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.save(vehicleModel));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateVehicle(@PathVariable UUID id, @RequestBody @Valid VehicleDTO vehicleDTO) {
        Optional<VehicleModel> vehicleModelOptional = this.vehicleService.getOne(id);
        if(!vehicleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not fount!");
        }

        VehicleModel vehicleModel = vehicleModelOptional.get();

        VehicleModel vehicleNewModel = new VehicleModel();
        BeanUtils.copyProperties(vehicleDTO, vehicleNewModel);
        vehicleNewModel.setId(vehicleModel.getId());

        return ResponseEntity.status(HttpStatus.OK).body(vehicleService.save(vehicleNewModel));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> removeVehicle(@PathVariable UUID id) {
        Optional<VehicleModel> vehicleModelOptional = this.vehicleService.getOne(id);
        if(!vehicleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found!");
        }

        vehicleService.delete(vehicleModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Vehicle removed successfully!");
    }

    @GetMapping()
    public ResponseEntity<Object> getOneVehicle() {
        List<VehicleModel> vehicles = this.vehicleService.getAll();
        if(vehicles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicles not fount!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneVehicle(@PathVariable UUID id) {
        Optional<VehicleModel> vehicleModelOptional = this.vehicleService.getOne(id);
        if(!vehicleModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not fount!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(vehicleModelOptional.get());
    }

    @PostMapping("/{licensePlate}/rent")
    public ResponseEntity<Object> rent(@PathVariable String licensePlate) {
        try {
            RentModel rent = rentService.rent(licensePlate);
            return ResponseEntity.status(HttpStatus.OK).body(rent);
        } catch (VehicleException error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
        }
    }

    @PostMapping("/{licensePlate}/return")
    public ResponseEntity<Object> returnVehicle(@PathVariable String licensePlate) {
        try {
            RentModel rent = rentService.returnVehicle(licensePlate);
            return ResponseEntity.status(HttpStatus.OK).body(rent);
        } catch (VehicleException | RentException error) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
        }
    }


}
