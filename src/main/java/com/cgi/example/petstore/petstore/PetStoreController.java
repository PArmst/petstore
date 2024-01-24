package com.cgi.example.petstore.petstore;

import com.cgi.example.petstore.api.PetStoreApi;
import com.cgi.example.petstore.model.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PetStoreController implements PetStoreApi {


    @Override
    public ResponseEntity<List<Pet>> findPetsByStatus(@RequestParam(name = "status") String status) {
        // Creating a dummy list of pets
        List<Pet> allPets = List.of(
                createDummyPet(1L, "Kitty", "CAT", "SOLD"),
                createDummyPet(2L, "Doggie", "DOG", "PENDING_COLLECTION"),
                createDummyPet(3L, "Piggie", "GUINEA_PIG", "AVAILABLE_FOR_PURCHASE")
        );

        List<Pet> filteredPets = allPets.stream()
                .filter(pet -> pet.getPetStatus().equals(Pet.PetStatusEnum.valueOf(status)))
                .toList();

        return ResponseEntity.ok(filteredPets);
    }

    public ResponseEntity<Pet> getPetById(Long petId) {

        Pet newPet = new Pet();
        newPet.setId(petId);
        newPet.setName("Kitty");
        newPet.setPetStatus(Pet.PetStatusEnum.SOLD);
        newPet.setPetType(Pet.PetTypeEnum.CAT);

        return ResponseEntity.ok(newPet);
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