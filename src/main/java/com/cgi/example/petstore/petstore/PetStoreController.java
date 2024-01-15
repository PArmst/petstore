package com.cgi.example.petstore.petstore;

import com.cgi.example.petstore.api.PetStoreApi;
import com.cgi.example.petstore.model.Pet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetStoreController implements PetStoreApi {
    @Override
    public ResponseEntity<Pet> getPetById(Long petId) {
        if (petId == null || petId <1 || petId >2000){
            return (ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        }

        Pet newPet = new Pet();
        newPet.setId(petId);
        newPet.setName("Static Name");

        return ResponseEntity.ok(newPet);
    }
}
