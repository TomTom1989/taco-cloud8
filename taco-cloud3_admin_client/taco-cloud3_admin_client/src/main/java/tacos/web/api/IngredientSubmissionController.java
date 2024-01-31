package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.filter.ValueNodes.JsonNode;

import tacos.IngredientData;
import tacos.data.IngredientService;
import tacos.Ingredient;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path="/api/ingredients", produces="application/json")
@CrossOrigin(origins="*")
public class IngredientSubmissionController {
    
    private static final Logger logger = LoggerFactory.getLogger(IngredientSubmissionController.class);

    private final RestTemplate restTemplate;
    private final IngredientService ingredientService;
    private final String clientId = "taco-admin-client";
    private final String clientSecret = "secret";
    private final String tokenUri = "http://localhost:9000/oauth2/token";

    @Autowired
    public IngredientSubmissionController(RestTemplate restTemplate, IngredientService ingredientService) {
        this.restTemplate = restTemplate;
        this.ingredientService = ingredientService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> handleAuthorizationCode(
            @RequestParam("authorizationCode") String authorizationCode) {
        try {
            logger.info("Received authorization code: {}", authorizationCode);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(clientId, clientSecret);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "authorization_code");
            map.add("code", authorizationCode);
            map.add("redirect_uri", "http://127.0.0.1:9000/api/show-submission-form");

            logger.info("Preparing token exchange request with headers: {}", headers);
            logger.info("Request body parameters: {}", map);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            logger.info("Sending token exchange request to URI: {}", tokenUri);

            ResponseEntity<String> rawResponse = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                request,
                String.class
            );

            logger.info("Token exchange response status code: {}", rawResponse.getStatusCode());
            logger.info("Token exchange response headers: {}", rawResponse.getHeaders());
            logger.info("Token exchange raw response body: {}", rawResponse.getBody());

            if (rawResponse.getBody() != null) {
                // Parse the raw JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode jsonResponse = objectMapper.readTree(rawResponse.getBody());

                // Check if the JSON response contains the access token
                if (jsonResponse.has("access_token")) {
                    String accessToken = jsonResponse.get("access_token").asText();
                    logger.info("Token exchange successful. Access Token: {}", accessToken);
                    return ResponseEntity.ok(Collections.singletonMap("accessToken", accessToken));
                }
            }

            // Log additional details if the token is missing
            logger.error("Token exchange failed. Status: {}, Headers: {}, Body: {}",
                    rawResponse.getStatusCode(), rawResponse.getHeaders(), rawResponse.getBody());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (HttpClientErrorException ex) {
            logger.error("HTTP Client Error during token exchange: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (HttpServerErrorException ex) {
            logger.error("HTTP Server Error during token exchange: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Exception occurred during token exchange: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SCOPE_writeIngredients')")
    public ResponseEntity<?> createIngredient(@RequestBody IngredientData ingredientData) {
        Ingredient createdIngredient = ingredientService.createIngredient(ingredientData);
        if (createdIngredient != null) {
            return new ResponseEntity<>(createdIngredient, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
