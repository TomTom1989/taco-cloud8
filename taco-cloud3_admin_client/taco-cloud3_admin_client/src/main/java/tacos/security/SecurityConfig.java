package tacos.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UriUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tacos.AppUser;
import tacos.data.UserRepository;
//import tacos.service.CustomLogoutHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
//	@Autowired
   // private CustomLogoutHandler customLogoutHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            AppUser user = userRepo.findByUsername(username);
            if (user != null) return user;
            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

  
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests -> {
                authorizeRequests
                    // Protect POST requests to /api/ingredients with the writeIngredients scope
                    .requestMatchers(new AntPathRequestMatcher("/api/ingredients", "POST"))
                    .hasAuthority("SCOPE_writeIngredients")
                    // Secure GET requests to /api/ingredients (adjust authority as needed)
                    .requestMatchers(new AntPathRequestMatcher("/api/ingredients", "GET"))
                    .authenticated() // or .hasAuthority("SCOPE_yourScope") if a specific scope is required
                    // Secure the /api/submit-form endpoint
                    .requestMatchers(new AntPathRequestMatcher("/api/show-submission-form")).authenticated()
                    // Existing matchers
                    .requestMatchers(new AntPathRequestMatcher("/design", "GET")).hasRole("USER")
                    .requestMatchers(new AntPathRequestMatcher("/orders", "GET")).hasRole("USER")
                    .requestMatchers(new AntPathRequestMatcher("/data-api/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/**")).permitAll();
            })
            // Continue with the existing formLogin, oauth2Login, logout, csrf, and headers configurations
            .formLogin(formLogin -> formLogin
                    .loginPage("/login")
                    .successHandler(successHandler()) // Set the custom success handler
                    .permitAll()
                
            )
            .oauth2Login(oauth2Login -> oauth2Login
                    .loginPage("/login")
                    .successHandler(successHandler()) // Set the custom success handler
                    .permitAll()
                )
            .oauth2ResourceServer(oauth2ResourceServer ->
                oauth2ResourceServer
                    .jwt(jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                    )
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/?logout")
                  //  .addLogoutHandler(customLogoutHandler) // Add the custom logout handler
                )
            .csrf().disable()
            .headers().frameOptions().sameOrigin();

        return http.build();
    }



    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            // URL-encode the exception message for query parameters
            String encodedErrorMessage = UriUtils.encodeQueryParam(exception.getMessage(), StandardCharsets.UTF_8.name());
            
            // Redirect to the login page with the encoded error message
            response.sendRedirect("/login?error=" + encodedErrorMessage);
        };
    }

    /*@Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOPE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scp"); // Default claim name for scope in JWT

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtConverter;
    }*/
    
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // Configure the converter if needed
        return converter;
    }


    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                // Redirect to the form submission page
                getRedirectStrategy().sendRedirect(request, response, "/show-submission-form");
            }
        };
    }



    
    
    }


    