package com.example.thirdweekhw;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("vehicles")
public class VehicleApi {

    private List<Vehicle> vehicleList;

    public VehicleApi() {
        vehicleList = new ArrayList<>();
        vehicleList.add(new Vehicle(1L, "Ford", "Fiesta", "Red"));
        vehicleList.add(new Vehicle(2L, "Toyota", "Corolla", "Black"));
        vehicleList.add(new Vehicle(3L, "Volkswagen", "Passat", "White"));
    }

    @GetMapping(produces = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Vehicle>> getVehicles() {
        return new ResponseEntity<>(vehicleList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable long id) {
        Optional<Vehicle> carById = vehicleList.stream().filter(vehicle -> vehicle.getId() == id).findFirst();
        return carById.map(vehicle -> new ResponseEntity<>(vehicle, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/colors/{color}")
    public ResponseEntity<List<Vehicle>> getVehicleByColor(@PathVariable String color) {
        List<Vehicle> carsByColor = vehicleList.stream().filter(vehicle -> vehicle.getColor().equals(color)).collect(Collectors.toList());
        if (carsByColor.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carsByColor, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addVehicle(@RequestBody Vehicle vehicle) {
        boolean add = vehicleList.add(vehicle);
        if (add) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity modVehicle(@RequestBody Vehicle newVehicle) {
        Optional<Vehicle> carById = vehicleList.stream().filter(vehicle -> vehicle.getId() == newVehicle.getId()).findFirst();
        if (carById.isPresent()) {
            vehicleList.remove(carById.get());
            vehicleList.add(newVehicle);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Vehicle> modVehicleColor(@RequestParam String color, @PathVariable long id) {
        Optional<Vehicle> carById = vehicleList.stream().filter(vehicle -> vehicle.getId() == id).findFirst();
        if (carById.isPresent()) {
            carById.get().setColor(color);
            return new ResponseEntity<>(carById.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vehicle> removeVehicle(@PathVariable long id) {
        Optional<Vehicle> carById = vehicleList.stream().filter(vehicle -> vehicle.getId() == id).findFirst();
        if (carById.isPresent()) {
            vehicleList.remove(carById.get());
            return new ResponseEntity<>(carById.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}