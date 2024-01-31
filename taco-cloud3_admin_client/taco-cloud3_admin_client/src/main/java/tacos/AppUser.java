package tacos;
import java.util.Arrays;
import java.util.Collection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.
 SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name="APPUSER")
@Data
//@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@RequiredArgsConstructor
public class AppUser implements UserDetails {
	
	public AppUser(String username, String password, String fullname, String street, String city, String state, String zip, String phoneNumber) {
	        this.username = username;
	        this.password = password;
	        this.fullname = fullname;
	        this.street = street;
	        this.city = city;
	        this.state = state;
	        this.zip = zip;
	        this.phoneNumber = phoneNumber;
	    }
	 
	 
	    // No-arg constructor
	 public AppUser() {
		this.password = "";
		this.fullname = "";
		this.street = "";
		this.city = "";
		this.state = "";
		this.zip = "";
		this.phoneNumber = "";
	    }
	 
	 
 private static final long serialVersionUID = 1L;
 @Id
 @GeneratedValue(strategy=GenerationType.AUTO)
 private Long id;
 private   String username;
 private final String password;
 private final String fullname;
 private final String street;
 private final String city;
 private final String state;
 private final String zip;
 private final String phoneNumber;
 @Override
 public Collection<? extends GrantedAuthority> getAuthorities() {
 return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
 }
 @Override
 public boolean isAccountNonExpired() {
 return true;
 }
 @Override
 public boolean isAccountNonLocked() {
 return true;
 }
 @Override
 public boolean isCredentialsNonExpired() {
 return true;
 }
 @Override
 public boolean isEnabled() {
 return true;
 }
 public Long getId() {
	 return id;
 }
public void setUsername(String email) {
	this.username=email;
}

}
