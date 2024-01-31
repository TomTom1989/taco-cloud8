package tacos.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import aj.org.objectweb.asm.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tacos.AppUser;
import tacos.Ingredient;
import tacos.IngredientData;

import org.springframework.security.core.Authentication;




@Service
public class IngredientService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private IngredientRepository ingredientRepository;
	
	@Autowired
    private AppUserService appUserService;
	

	
	public Ingredient createIngredient(IngredientData ingredientData) {
	    AppUser user = userRepository.findByUsername(ingredientData.getUsername());
	    if (user != null) {
	        Long appUserId = user.getId();
	        Ingredient ingredient = new Ingredient(ingredientData.getId(), ingredientData.getName(), ingredientData.getType(), appUserId);
	        return ingredientRepository.save(ingredient);
	    } else {
	        // Handle the scenario where the user is not found
	        return null;
	    }
	}
	

	

	    /*public Ingredient createIngredient(String ingredientId, String name, Ingredient.Type type, String username) {
	        AppUser user = userRepository.findByUsername(username);
	        if (user != null) {
	            Long appUserId = user.getId();
	            Ingredient ingredient = new Ingredient(ingredientId, name, type, appUserId);
	            return ingredientRepository.save(ingredient);
	        } else {
	            // Handle the scenario where the user is not found
	            return null;
	        }
	    }*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	    public List<Ingredient> getIngredientsForUserAndBasic(Long userId) {
	        // Fetch ingredients created by the user
	        List<Ingredient> userIngredients = ingredientRepository.findByAppUserId(userId);

	        // Fetch basic ingredients (no specific user)
	        List<Ingredient> basicIngredients = ingredientRepository.findByAppUserIdIsNull();

	        // Combine both lists ensuring no duplication
	        Set<Ingredient> combinedIngredients = new HashSet<>(userIngredients);
	        combinedIngredients.addAll(basicIngredients);

	        return new ArrayList<>(combinedIngredients);
	    }


   /* public List<Ingredient> getIngredientsForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        AppUser currentUser = userRepository.findByUsername(currentUsername); // Assuming you have a method to find user by username
        return ingredientRepository.findByUser(currentUser);
    }*/
	
   /* public void addIngredient(Ingredient ingredient) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
       // AppUser currentUser = userRepository.findByUsername(currentUsername);
        ingredient.setUsername(currentUsername);
        ingredientRepository.save(ingredient);
    }*/

    
	/*public Ingredient addIngredient(Ingredient ingredient, Authentication authentication) {
	    String currentUsername = authentication.getName();
	    AppUser currentUser = userRepository.findByUsername(currentUsername);
	    if (currentUser != null) {
	        // Instead of setting the user, set the user ID if you have such a field
	        ingredient.setAppUserId(currentUser.getId());
	        return ingredientRepository.save(ingredient);
	    } else {
	        throw new UsernameNotFoundException("User not found");
	    }
	}*/
	
	
	/*private static final Logger log = LoggerFactory.getLogger(IngredientService.class);
    private final RestTemplate restTemplate;
 

    public IngredientService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
   
    }*/
    
   /* public List<Ingredient> getIngredientsForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AppUser user = userRepository.findByUsername(username);

        if (user != null) {
            return ingredientRepository.findByAppUserId(user.getId());
        }
        return new ArrayList<>();
    }*/

    

    // GET HTTP request with getForObject:
    
    /*public Ingredient getIngredientById(String ingredientId) {
        return restTemplate.getForObject(
            "http://localhost:8080/data-api/ingredients/{id}",
            Ingredient.class, ingredientId);
    }*/
    
 
 /*   //GET HTTP request with getForEntity:
    public Ingredient getIngredientById(String ingredientId) {
    	 ResponseEntity<Ingredient> responseEntity =
    	 restTemplate.getForEntity("http://localhost:9000/data-api/ingredients/{id}",
    	 Ingredient.class, ingredientId);
    	 log.info("Fetched time: " +
    	 responseEntity.getHeaders().getDate());
    	 return responseEntity.getBody();
    	}*/
    
    // method inside HTTP GET
 /*   public Iterable<Ingredient> allIngredients() {
    	 return Arrays.asList(restTemplate.getForObject(
    	 "http://localhost:9000/data-api/ingredients",
    	 Ingredient[].class));
    	 }*/
    
    
    //method inside PUT HTTP request:
   /* public void updateIngredient(Ingredient ingredient) {
    	 restTemplate.put("http://localhost:9000/data-api/ingredients/{id}",   //Actually it works with curl http://localhost:9000/api/ingredients

    	 ingredient, ingredient.getId());
    	}*/
    
    
 /*   //method inside DELETE HTTP request:
    public void deleteIngredient(Ingredient ingredient) {
    	 restTemplate.delete("http://localhost:9000/data-api/ingredients/{id}",
    	 ingredient.getId());
    	}
*/
    
    //method inside HTTP PUT:
  /*  public Ingredient createIngredient(Ingredient ingredient) {
    	 return restTemplate.postForObject("http://localhost:9000/data-api/ingredients",
    	 ingredient, Ingredient.class);
    	}
    
    //method inside HTTP PUT
    public Ingredient createIngredient2(Ingredient ingredient) {
    	 ResponseEntity<Ingredient> responseEntity =
    	 restTemplate.postForEntity("http://localhost:9000/data-api/ingredients",
    	 ingredient,
    	 Ingredient.class);
    	 log.info("New resource created at " +
    	 responseEntity.getHeaders().getLocation());
    	 return responseEntity.getBody();
    	}
    */
    
   /* public List<Ingredient> getAllIngredients() {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "http://localhost:8080/data-api/ingredients", String.class);
            
            System.out.println("Response: " + responseEntity.getBody()); // 

            IngredientCollectionDTO response = restTemplate.getForObject(
                "http://localhost:8080/data-api/ingredients",
                IngredientCollectionDTO.class);

            return response != null ? response.getIngredients() : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }*/

  /*  public List<Ingredient> getIngredientsForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ingredientRepository.findByUsername(currentUsername);
    }
*/
    
    }