package tacos;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;
import lombok.Data;


@Data
@Entity
public class TacoOrder implements Serializable {



	//@Column(name = "taco_names")
	private String tacoNames ;
	
	private static final long serialVersionUID = 1L;
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private Long id;
	 
	 private Date placedAt= new Date();
	
	
	
 @NotBlank(message="Delivery name is required")
 private String deliveryName;
 @NotBlank(message="Street is required")
 private String deliveryStreet;
 @NotBlank(message="City is required")
 private String deliveryCity;
 @NotBlank(message="State is required")
 private String deliveryState;
 @NotBlank(message="Zip code is required")
 private String deliveryZip;
 @CreditCardNumber(message="Not a valid credit card number")
 private String ccNumber;
 @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
 message="Must be formatted MM/YY")
 private String ccExpiration;
 
 @Column(name = "cc_cvv")
 @Digits(integer=3, fraction=0, message="Invalid CVV")
 private String ccCVV;
 
 @OneToMany(cascade = CascadeType.ALL)
 private List<Taco> tacos = new ArrayList<>();
 
 



 

/* public void addTaco(Taco taco) {
	    this.tacos.add(taco);
	    updateTacoNames(); 
	}
 
 private void updateTacoNames() {
     this.tacoNames = this.tacos.stream()
                         .map(Taco::getName)
                         .collect(Collectors.joining(", "));
 }*/
 
 public void addTacoName(String tacoName) {
     if (this.tacoNames == null || this.tacoNames.isEmpty()) {
         this.tacoNames = tacoName;
     } else {
         this.tacoNames += ", " + tacoName;
     }
 }
 
 public void setPlacedAt(Date placedAt) {
     this.placedAt = placedAt;
 }
 
 public Date getPlacedAt() {
     return this.placedAt;
 }
public void setId(long orderId) {
	id=orderId;
	
}

public void setTacoNames(String tacoNames) {
    this.tacoNames = tacoNames;
}

public String getTacoNames() {
    return tacoNames;
}
public void addTaco(Taco taco) {
    this.tacos.add(taco);
}

@Override
public String toString() {
    return "TacoOrder{" +
           "id=" + id +
           ", deliveryName='" + deliveryName + '\'' +
           ", tacoNames='" + tacoNames + '\'' +
           // add other fields if necessary
           '}';
}
}