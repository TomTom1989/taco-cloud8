package tacos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.qos.logback.core.subst.Token.Type;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

@Data
@Entity
@RestResource(rel="tacos", path="tacos")
public class Taco implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ManyToOne
    private TacoOrder tacoOrder;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;
    private Date createdAt = new Date();
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    @ManyToMany()
    private List<Ingredient> ingredients = new ArrayList<>();

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
    
    public void setTacoOrder(TacoOrder tacoOrder) {
        this.tacoOrder = tacoOrder;
    }
    
    public String getName() {
    	return name;
    }
    
    @Override
    public String toString() {
        return "Taco{" +
               "id=" + id +  // Assuming you have an 'id' field.
               ", name='" + name + '\'' +  // Assuming you have a 'name' field.
               ", ingredients=" + ingredients +  // Assuming you have an 'ingredients' collection.
               // ... other fields you want to include
               '}';
    }
    
   
    
    
    
    
    
}
