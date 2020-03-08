package com.lab2webservices.lab2webservices;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
class CarDataModelAssembler implements RepresentationModelAssembler<Car, EntityModel<Car>> {

    @Override
    public EntityModel<Car> toModel(Car car) {
        return new EntityModel<>(car,
                linkTo(methodOn(CarController.class).one(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).all()).withRel("cars"));
    }

    @Override
    public CollectionModel<EntityModel<Car>> toCollectionModel(Iterable<? extends Car> entities) {
        var collection = StreamSupport.stream(entities.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(collection,
                linkTo(methodOn(CarController.class).all()).withSelfRel());
    }
}