package tacos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import tacos.data.AppUserService;
import tacos.data.IngredientRepository;
//import tacos.service.ConsentService;
import tacos.Ingredient.Type;

@SpringBootApplication
public class TacoCloudApplication {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private IngredientRepository ingredientRepository;

    public static void main(String[] args) {
        SpringApplication.run(TacoCloudApplication.class, args);
    }

}
