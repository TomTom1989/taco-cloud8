package tacos.web.api;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import tacos.IngredientData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequestMapping("/api")
public class UIController {

    private final RestTemplate restTemplate;
    private final String clientId = "taco-admin-client"; 
    private final String clientSecret = "secret"; 
    private final String tokenUri = "http://localhost:9000/oauth2/token";
    private final String ingredientCreateUri = "http://localhost:9000/api/ingredients/create";

    @Autowired
    public UIController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/submit-form")
    public ResponseEntity<?> handleSubmit(@RequestParam("authorizationCode") String authorizationCode,
                                          @RequestBody IngredientData ingredientData) {
        // Exchange code for token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", "http://127.0.0.1:9000");
        map.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUri, request, Map.class);
        
        // Extract access token from tokenResponse
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // Use token to make POST request to /api/ingredients/create
        HttpHeaders ingredientHeaders = new HttpHeaders();
        ingredientHeaders.setContentType(MediaType.APPLICATION_JSON);
        ingredientHeaders.setBearerAuth(accessToken);

        HttpEntity<IngredientData> ingredientRequest = new HttpEntity<>(ingredientData, ingredientHeaders);
        ResponseEntity<String> ingredientResponse = restTemplate.postForEntity(ingredientCreateUri, ingredientRequest, String.class);

        return ResponseEntity.ok().body(ingredientResponse.getBody());
    }
    
    
    @GetMapping("/show-submission-form")
    public String showSubmissionForm() {
        return "submission";
    }

}
