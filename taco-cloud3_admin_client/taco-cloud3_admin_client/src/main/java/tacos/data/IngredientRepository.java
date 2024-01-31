package tacos.data;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import tacos.AppUser;
import tacos.Ingredient;


public interface IngredientRepository extends JpaRepository<Ingredient, String> {
	//List<Ingredient> findByUsername(String user);
	List<Ingredient> findByAppUserId(Long appUserId);
	 List<Ingredient> findByAppUserIdIsNull();
}
