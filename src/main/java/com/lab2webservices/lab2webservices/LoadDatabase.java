package com.lab2webservices.lab2webservices;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(CarRepository carRepo) {
        return args -> {
            carRepo.save(new Car(0L, "Audi A6", 1));
            carRepo.save(new Car(0L, "Mercedes SL", 2));
        };
    }
}
