package tacos.web.api;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tacos.Ingredient;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;


@RestController
@RequestMapping(path="/api/tacos", produces="application/json")
@CrossOrigin(origins="*") 
public class TacoController {
 private TacoRepository tacoRepo;
 private OrderRepository orderRepo;
 
 public TacoController(TacoRepository tacoRepo, OrderRepository orderRepo) {
 this.tacoRepo = tacoRepo;
 this.orderRepo=orderRepo;
 }
 @GetMapping(params="recent")
 public Iterable<Taco> recentTacos() { 
 PageRequest page = PageRequest.of(
 0, 12, Sort.by("createdAt").descending());
 return tacoRepo.findAll(page).getContent();
 }
 
 @GetMapping("/{id}")
 public ResponseEntity<Taco> tacoById(@PathVariable Long id) {
  Optional<Taco> optTaco = tacoRepo.findById(id);
  if (optTaco.isPresent()) {
  return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
  }
  return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
 }

 @PostMapping(consumes="application/json")
 @ResponseStatus(HttpStatus.CREATED)
 public Taco postTaco(@RequestBody Taco taco) {
  return tacoRepo.save(taco);
 }

 @PutMapping(path="/{orderId}", consumes="application/json")
 public TacoOrder putOrder(
  @PathVariable Long orderId,
  @RequestBody TacoOrder order) {
  order.setId(orderId);
  return orderRepo.save(order);
 }
 
 
 @DeleteMapping("/{orderId}")
 @ResponseStatus(HttpStatus.NO_CONTENT)
 public void deleteOrder(@PathVariable Long orderId) {
  try {
  orderRepo.deleteById(orderId);
  } catch (EmptyResultDataAccessException e) {}
 }

 
 @PatchMapping(path="/{orderId}", consumes="application/json")
 public TacoOrder patchOrder(@PathVariable Long orderId,
  @RequestBody TacoOrder patch) {
  TacoOrder order = orderRepo.findById(orderId).get();
  if (patch.getDeliveryName() != null) {
  order.setDeliveryName(patch.getDeliveryName());
  }
  if (patch.getDeliveryStreet() != null) {
  order.setDeliveryStreet(patch.getDeliveryStreet());
  }
  if (patch.getDeliveryCity() != null) {
  order.setDeliveryCity(patch.getDeliveryCity());
  }
  if (patch.getDeliveryState() != null) {
  order.setDeliveryState(patch.getDeliveryState());
  }
  if (patch.getDeliveryZip() != null) {
  order.setDeliveryZip(patch.getDeliveryState());
  }
  if (patch.getCcNumber() != null) {
  order.setCcNumber(patch.getCcNumber());
  }
  if (patch.getCcExpiration() != null) {
  order.setCcExpiration(patch.getCcExpiration());
  }
  if (patch.getCcCVV() != null) {
  order.setCcCVV(patch.getCcCVV());
  }
  return orderRepo.save(order);
 }
 
 
 /*@Bean
 public CommandLineRunner dataLoader(
  IngredientRepository repo,
  UserRepository userRepo,
  PasswordEncoder encoder,
  TacoRepository tacoRepo) {
  return args -> {
  Ingredient flourTortilla = new Ingredient(
  "FLTO", "Flour Tortilla", Type.WRAP);
  Ingredient cornTortilla = new Ingredient(
  "COTO", "Corn Tortilla", Type.WRAP);
  Ingredient groundBeef = new Ingredient(
  "GRBF", "Ground Beef", Type.PROTEIN);
  Ingredient carnitas = new Ingredient(
  "CARN", "Carnitas", Type.PROTEIN);
  Ingredient tomatoes = new Ingredient(
  "TMTO", "Diced Tomatoes", Type.VEGGIES);
  Ingredient lettuce = new Ingredient(
  "LETC", "Lettuce", Type.VEGGIES);
  Ingredient cheddar = new Ingredient(
  "CHED", "Cheddar", Type.CHEESE);
  Ingredient jack = new Ingredient(
  "JACK", "Monterrey Jack", Type.CHEESE);
  Ingredient salsa = new Ingredient(
  "SLSA", "Salsa", Type.SAUCE);
  Ingredient sourCream = new Ingredient(
  "SRCR", "Sour Cream", Type.SAUCE);
  repo.save(flourTortilla);
  repo.save(cornTortilla);
  repo.save(groundBeef);
  repo.save(carnitas);
  repo.save(tomatoes);
  repo.save(lettuce);
  repo.save(cheddar);
  repo.save(jack);
  repo.save(salsa);
  repo.save(sourCream);
  
  Taco taco1 = new Taco();
  taco1.setName("Carnivore");
  taco1.setIngredients(Arrays.asList(
  flourTortilla, groundBeef, carnitas,
  sourCream, salsa, cheddar));
  tacoRepo.save(taco1);
  Taco taco2 = new Taco();
  taco2.setName("Bovine Bounty");
  taco2.setIngredients(Arrays.asList(
  cornTortilla, groundBeef, cheddar,
  jack, sourCream));
  tacoRepo.save(taco2);
  Taco taco3 = new Taco();
  taco3.setName("Veg-Out");
  taco3.setIngredients(Arrays.asList(
  flourTortilla, cornTortilla, tomatoes,
  lettuce, salsa));
  tacoRepo.save(taco3);
  };
}*/
}
