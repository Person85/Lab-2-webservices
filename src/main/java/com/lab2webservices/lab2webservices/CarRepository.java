package com.lab2webservices.lab2webservices;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByCarName(String carName);

    boolean existsCarByCarName(String carName);

}
