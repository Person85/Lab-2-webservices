package com.lab2webservices.lab2webservices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@WebMvcTest(CarController.class)
@Import({CarDataModelAssembler.class})
class CarControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CarRepository repository;

    @BeforeEach
    void setUpNewTest() {
        when(repository.findAll()).thenReturn(List.of(new Car(1L, "Audi A6", 1),
                new Car(2L, "Mercedes SL", 2)));
        when(repository.findById(1L)).thenReturn(Optional.of(new Car(1L, "Audi A6", 1)));
        when(repository.findByCarName("Audi A6")).thenReturn(Optional.of(new Car(1L, "Audi A6",1)));
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(Car.class))).thenAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            var p = (Car) args[0];
            return new Car(1L, p.getCarName(), p.getBrandId());
        });
    }

    @Test
    void getAllReturnsListOfAllCars() throws Exception {
        mockMvc.perform(
                get("/api/v1/cars").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.carList[0]._links.self.href", is("http://localhost/api/v1/cars/1")))
                .andExpect(jsonPath("_embedded.carList[0].carName", is("Audi A6")));
    }

    @Test
    @DisplayName("Calls Get method for car with id 1")
    void getOnecarWithValidIdOne() throws Exception {
        mockMvc.perform(
                get("/api/v1/cars/1").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href", is("http://localhost/api/v1/cars/1")));
    }

    @Test
    @DisplayName("Calls Get method for car with invalid id")
    void getOnecarWithInValidIdThree() throws Exception {
        mockMvc.perform(
                get("/api/cars/0").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addNewcarWithPostReturnsCreatedcar() throws Exception {
        mockMvc.perform(
                post("/api/v1/cars/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0,\"name\":\"Audi A8\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Delete car with ID 1")
    void deleteCarInRepository() throws Exception {
        mockMvc.perform(delete("/api/v1/cars/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Trying to delete car with invalid ID")
    void deleteCarWithInvalidID() throws Exception {
        mockMvc.perform(delete("/api/v1/cars/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Put with complete data")
    void putCarWithCompleteDataWithId1() throws Exception {
        mockMvc.perform(put("/api/v1/cars/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"carName\":\"Audi A8\",\"brandId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href", is("http://localhost/api/v1/cars/1")))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("carName", is("Audi A8")))
                .andExpect(jsonPath("brandId", is(1)));
    }

    @Test
    @DisplayName("Put with incomplete data, should return brand id 0 on missing content")
    void putCarWithIncompleteData() throws Exception {
        mockMvc.perform(put("/api/v1/cars/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"carName\":\"Audi A8\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href", is("http://localhost/api/v1/cars/1")))
                        .andExpect(jsonPath("carName", is("Audi A8")))
                        .andExpect(jsonPath("brandId", is(0)));
    }

    @Test
    @DisplayName("Patch car with new complete data")
    void patchCarWithAllData() throws Exception {
        mockMvc.perform(patch("/api/v1/cars/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"carName\":\"Audi A8\",\"brandId\":\"1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href", is("http://localhost/api/v1/cars/1")))
                .andExpect(jsonPath("carName", is("Audi A8")));
    }

    @Test
    @DisplayName("Patch with only carname and expect other values to remain unchanged")
    void patchCarWithNewCarname() throws Exception {
        mockMvc.perform(patch("/api/v1/cars/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"carName\":\"Audi A8\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self.href", is("http://localhost/api/v1/cars/1")))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("carName", is("Audi A8")))
                .andExpect(jsonPath("brandId", is(1)));
    }
}