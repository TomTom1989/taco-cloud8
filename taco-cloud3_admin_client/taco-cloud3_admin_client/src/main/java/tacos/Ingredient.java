package tacos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient implements Serializable {
    
   
  //  @Column(name = "appuser_id")
    //private AppUser user;
    
    // New field to store AppUser's ID

    public Ingredient(String id, String name, Type sauce, Long appUserid) {
        this.id = id;
        this.name = name;
        this.type = sauce;
        this.appUserId= appUserid;
        
    }



    @Id
    private String id;
    private String name;
    private Type type;
    private Long appUserId;

    @ManyToMany(mappedBy = "ingredients", cascade = CascadeType.ALL)
    private List<Taco> tacos;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

	
}
