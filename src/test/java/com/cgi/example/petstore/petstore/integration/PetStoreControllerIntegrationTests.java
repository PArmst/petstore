package com.cgi.example.petstore.petstore.integration;

import com.cgi.example.petstore.model.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"management.server.port=0"})
@ExtendWith(SpringExtension.class)
public class PetStoreControllerIntegrationTests {

    @Autowired
    private Environment environment;

    @Autowired
    private TestRestTemplate testRestTemplate;


    private String getBaseUrl() {
        // append the port number to the base url
        return "http://localhost:" + getServerPort();
    }

    private int getServerPort() {
        //generate a random port number from the environment variable and return it
        return Integer.parseInt(environment.getProperty("local.server.port"));
    }

    @Test
    public void testGetPetByIdEndpoint() {
        // creating a petID for test
        Long petId = 123L;

        // send a request to the full url
        ResponseEntity<Pet> responseEntity = testRestTemplate.getForEntity(
                getBaseUrl() + "/api/v1/pet-store/pets/{petId}",
                Pet.class,
                petId
        );

        // assert status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // make sure the body of the response is not empty
        assertNotNull(responseEntity.getBody());
        // assert the petID is correct in the response
        assertEquals(petId, responseEntity.getBody().getId());
        // assert the correct pet name is being returned
        assertEquals("Static Name", responseEntity.getBody().getName());
    }

    @Test
    public void testValidPetID() {
        // assign a valid petID
        Long validID = 1584L;

        // Send a request
        ResponseEntity<Pet> responseEntity = testRestTemplate.getForEntity(
                getBaseUrl() + "/api/v1/pet-store/pets/{petId}",
                Pet.class,
                validID
        );

        // assert status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testInvalidPetID() {
        // assign a valid petID
        Long invalidID = 3229L;

        // Send a request
        ResponseEntity<Pet> responseEntity = testRestTemplate.getForEntity(
                getBaseUrl() + "/api/v1/pet-store/pets/{petId}",
                Pet.class,
                invalidID
        );

        // assert status code
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        if (responseEntity.getBody() != null) {
            System.out.println("Response Body: " + responseEntity.getBody());
        }
    }

    @Test
    public void testFindPetByStatus() throws Exception {
        //defining expected data
        List<Pet> expectedPets = List.of(
                createDummyPet(1L, "Kitty", "CAT", "SOLD"),
                createDummyPet(2L, "Doggie", "DOG", "PENDING_COLLECTION"),
                createDummyPet(3L, "Piggie", "GUINEA_PIG", "AVAILABLE_FOR_PURCHASE")
        );

        //filter for the number of "SOLD" pets
        long expectedSoldCount = expectedPets.stream().filter(pet -> Pet.PetStatusEnum.SOLD.equals(pet.getPetStatus())).count();

        String url = getBaseUrl() + "/api/v1/pet-store/pets/findByStatus?status=SOLD";
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, String.class);

        // Assert status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Print the received JSON for debugging
        System.out.println("Received JSON: " + responseEntity.getBody());

        // Convert the received JSON to an ArrayNode for further inspection
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.readValue(responseEntity.getBody(), ArrayNode.class);

        // Assert response body
        assertNotNull(arrayNode);
        assertEquals(expectedSoldCount, arrayNode.size());
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


// the following is what I tried first, this did not work and consistently threw errors related to status codes.
// It was returning a 302 (redirect) instead of a 200. Still not sure why.

/*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"management.server.port=0"})
@ExtendWith(SpringExtension.class)
public class ApplicationIntegrationTests {
    @Autowired
    private Environment environment;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private String getBaseURL(){
        String port = environment.getProperty("local.server.port");
        return ("http://localhost"+port+"/api/v1/pet-store");
    }

    @Test
    public void testGetPetById() throws JsonProcessingException {
        //use the base URL and append endpoint path
        String url = getBaseURL()+"/pets/1";

        //Send a GET request using TestRestTemplate
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(url, String.class);


        //Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


        //Deserialize the JSON response
        Pet petResponse = objectMapper.readValue(responseEntity.getBody(), Pet.class);

        assertEquals(1L, petResponse.getId());
        assertEquals("Static Name", petResponse.getName());
    }
}
*/

