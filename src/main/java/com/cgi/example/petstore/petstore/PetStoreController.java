package com.cgi.example.petstore.petstore;

import com.cgi.example.petstore.api.PetStoreApi;
import com.cgi.example.petstore.model.Pet;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetStoreController implements PetStoreApi {

    public ResponseEntity<Pet> getPetById(Long petId) {

        Pet newPet = new Pet();
        newPet.setId(petId);
        newPet.setName("Static Name");

        return ResponseEntity.ok(newPet);
    }
}