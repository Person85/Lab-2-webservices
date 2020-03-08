package com.lab2webservices.lab2webservices;

import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.hateoas.RepresentationModel;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Car {

    @Id @GeneratedValue private  Long id;
    private String carName;
    private int brandId;

    public Car(Long id, String carName, int brandId){
        this.id = id;
        this.carName = carName;
        this.brandId = brandId;
    }

    public String getCarName() {
        return carName;
    }

    public Long getId() {
        return id;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }
}
