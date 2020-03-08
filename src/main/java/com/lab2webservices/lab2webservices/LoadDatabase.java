package com.lab2webservices.lab2webservices;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(PhoneRepository phoneRepo) {
        return args -> {
            phoneRepo.save(new Phone(0L, "Iphone X", 1));
            phoneRepo.save(new Phone(0L, "Samsung Galaxy S10", 2));
        };
    }
}
