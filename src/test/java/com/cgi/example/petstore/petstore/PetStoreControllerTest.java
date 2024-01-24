package com.cgi.example.petstore.petstore;

import com.cgi.example.petstore.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PetStoreControllerTest {

    @InjectMocks
    private PetStoreController petStoreController;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findPetsByStatus_ShouldReturnFilteredPets() {
        // Arrange
        String status = "SOLD";
        List<Pet> expectedPets = List.of(
                createDummyPet(1L, "Kitty", "CAT", "SOLD")
        );

        // Act
        ResponseEntity<List<Pet>> responseEntity = petStoreController.findPetsByStatus(status);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPets, responseEntity.getBody());
    }

    @Test
    void getPetById_ShouldReturnPetById() {
        // Arrange
        Long petId = 1L;
        Pet expectedPet = createDummyPet(petId, "Kitty", "CAT", "SOLD");

        // Act
        ResponseEntity<Pet> responseEntity = petStoreController.getPetById(petId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPet, responseEntity.getBody());
    }

    private Pet createDummyPet(Long id, String name, String petType, String petStatus) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setPetType(Pet.PetTypeEnum.valueOf(petType));
        pet.setPetStatus(Pet.PetStatusEnum.valueOf(petStatus));
        return pet;
    }
}
