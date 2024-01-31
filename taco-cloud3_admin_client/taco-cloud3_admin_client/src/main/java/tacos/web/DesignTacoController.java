package tacos.web;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.data.AppUserService;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {
	
private final IngredientRepository ingredientRepo;
private final TacoRepository tacoRepository;
private final AppUserService appUserService;

public DesignTacoController(
        IngredientRepository ingredientRepo,
        TacoRepository tacoRepository,
        AppUserService appUserService) {
        this.ingredientRepo = ingredientRepo;
        this.tacoRepository = tacoRepository;
        this.appUserService = appUserService;
    }

@ModelAttribute("tacoOrder")
public TacoOrder tacoOrder() {
    return new TacoOrder(); // This ensures a new TacoOrder is in the model if one isn't already
}
	
@ModelAttribute
public void addIngredientsToModel(Model model) {
    Long currentUserId = appUserService.getCurrentUserId();
    List<Ingredient> userSpecificIngredients = null;
    List<Ingredient> generalIngredients = ingredientRepo.findByAppUserIdIsNull();

    if (currentUserId != null) {
        // Fetch ingredients specific to the user
        userSpecificIngredients = ingredientRepo.findByAppUserId(currentUserId);
    }

    // Combine user-specific ingredients with general ones
    List<Ingredient> combinedIngredients = Stream.concat(
        userSpecificIngredients != null ? userSpecificIngredients.stream() : Stream.empty(),
        generalIngredients.stream()
    ).collect(Collectors.toList());

    Type[] types = Ingredient.Type.values();
    for (Type type : types) {
        List<Ingredient> ingredientsList = combinedIngredients.stream()
                .filter(ingredient -> ingredient.getType().equals(type))
                .collect(Collectors.toList());
        model.addAttribute(type.toString().toLowerCase(), ingredientsList);
    }
}



//Spring MVC controller method
@GetMapping
public String showDesignForm(Model model) {
    if (!model.containsAttribute("tacoOrder")) {
        model.addAttribute("tacoOrder", new TacoOrder()); // This adds TacoOrder to the session
    }
    model.addAttribute("taco", new Taco()); // This adds a new Taco to the model for the form
    return "design";
}
 
//Spring MVC controller method
@PostMapping
public String processTaco(@Valid Taco taco, Errors errors,
                          @ModelAttribute TacoOrder tacoOrder,
                          Model model) {
    if (errors.hasErrors()) {
        return "design";
    }

    Taco savedTaco = tacoRepository.save(taco);
    tacoOrder.addTacoName(savedTaco.getName());
    model.addAttribute("tacoOrder", tacoOrder);

    return "redirect:/orders/current";
}


 
 
}