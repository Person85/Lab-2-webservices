package com.lab2webservices.lab2webservices;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface PhoneRepository extends JpaRepository<Phone, Long> {

    Optional<Phone> findByPhoneName(String phoneName);

    boolean existsPhoneByPhoneName(String phoneName);

}
