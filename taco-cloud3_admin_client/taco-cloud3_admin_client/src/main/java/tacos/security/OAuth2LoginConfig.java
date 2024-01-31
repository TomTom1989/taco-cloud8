package tacos.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import tacos.AppUser;
import tacos.data.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

@Configuration
public class OAuth2LoginConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.facebookClientRegistration());
    }

    private ClientRegistration facebookClientRegistration() {
        return ClientRegistration.withRegistrationId("facebook")
            .clientId("2291618721048050")
            .clientSecret("e2f74f246bd193335ee806c5419d1ee6")
            .scope("email", "public_profile")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .authorizationUri("https://www.facebook.com/v18.0/dialog/oauth")
            .tokenUri("https://graph.facebook.com/v18.0/oauth/access_token")
            .userInfoUri("https://graph.facebook.com/v18.0/me?fields=id,name,email,picture")
            .userNameAttributeName("id")
            .clientName("Facebook")
            .build();
    }
    
  /*  @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return (OAuth2UserRequest userRequest) -> {
            // Delegate to the default implementation for loading a user
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            // Fetch the authorities (roles) from somewhere or just assign a default role
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            // You could also look up the user's authorities from the database here
            mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            // Return a new OAuth2User with the assigned roles
            return new DefaultOAuth2User(mappedAuthorities, oAuth2User.getAttributes(), "name");
        };
    }*/     
        
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService(UserRepository userRepository) {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return (OAuth2UserRequest userRequest) -> {
            // Delegate to the default implementation for loading a user
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            // Extract email from OAuth2User
            String email = oAuth2User.getAttribute("email");

            // Fetch the authorities (roles) from somewhere or just assign a default role
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            // Check if user already exists in the database, if not create a new one
            AppUser appUser = userRepository.findByUsername(email);
            if (appUser == null) {
                appUser = new AppUser();
                appUser.setUsername(email); // Set the email as username
                // Set other properties as needed or leave them with default values
                userRepository.save(appUser);
            }

            // Return a new OAuth2User with the assigned roles
            return new DefaultOAuth2User(mappedAuthorities, oAuth2User.getAttributes(), "name");
        };
    }

   
}