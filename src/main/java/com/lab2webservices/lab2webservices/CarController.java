package com.lab2webservices.lab2webservices;



import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;



@RequestMapping("/api/v1/cars")
@RestController
public class CarController {

    private final CarDataModelAssembler carDataModelAssembler;
    private CarRepository repository;

    CarController(CarRepository repository, CarDataModelAssembler carDataModelAssembler) {
        this.repository = repository;
        this.carDataModelAssembler = carDataModelAssembler;
    }


    @GetMapping
    public CollectionModel<EntityModel<Car>> all() {
        return carDataModelAssembler.toCollectionModel(repository.findAll());
    }

    @GetMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<EntityModel<Car>> one(@PathVariable Long id) {
        return repository.findById(id)
                .map(carDataModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{carName:[\\D]+[\\d]*}")
    public ResponseEntity<EntityModel<Car>> oneOrMany(@PathVariable String carName) {
        return repository.findByCarName(carName)
                .map(carDataModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping(value = "/brand/{brandId}")
    public Optional<Car> one(@PathVariable int brandId) {
        return repository.findById((long) brandId);
    }

    @PostMapping
    ResponseEntity<Car> newCar(@RequestBody Car car) {
        if (repository.existsCarByCarName(car.getCarName()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        repository.save(car);
        var entityModelResponseEntity = repository.findById(car.getId())
                .map(carDataModelAssembler::toModel);
        return new ResponseEntity(entityModelResponseEntity, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    ResponseEntity<EntityModel<Car>> replaceCar(@RequestBody Car carIn, @PathVariable Long id) {

        if(repository.findById(id).isPresent()){
            var p = repository.findById(id)
                    .map(existingCar -> {
                        existingCar.setCarName(carIn.getCarName());
                        existingCar.setBrandId(carIn.getBrandId());
                        repository.save(existingCar);
                        return existingCar;})
                    .get();
            var entityModel = carDataModelAssembler.toModel(p);
            return new ResponseEntity<>(entityModel, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    ResponseEntity<EntityModel<Car>> modifyUser(@RequestBody Car updatedCar, @PathVariable Long id){
        if(repository.findById(id).isPresent()){
            var p = repository.findById(id)
                    .map(newCar -> {
                        if(updatedCar.getCarName() != null)
                            newCar.setCarName(updatedCar.getCarName());
                        if(updatedCar.getBrandId() != 0)
                            newCar.setBrandId(updatedCar.getBrandId());
                        repository.save(newCar);
                        return newCar;}).get();
            var entityModel = carDataModelAssembler.toModel(p);
            return new ResponseEntity<>(entityModel, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
