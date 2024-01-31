package tacos.authorization;


import java.security.KeyPair;
//import java.security.interfaces.RSAKey;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
//import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.JWKSet;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//@Import(OAuth2AuthorizationServerConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {
	
	private static final Logger log = LoggerFactory.getLogger(AuthorizationServerConfig.class);

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) 
    throws Exception {
        org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http
            .formLogin(Customizer.withDefaults())
            .build();
    }
   
   
    

    
  @Bean
    public RegisteredClientRepository registeredClientRepository(
     PasswordEncoder passwordEncoder) {
	  log.info("Creating RegisteredClientRepository");
     RegisteredClient registeredClient =
     RegisteredClient.withId(UUID.randomUUID().toString())
     .clientId("taco-admin-client")
     .clientSecret(passwordEncoder.encode("secret"))
     .clientAuthenticationMethod(
     ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
     .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
     .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
     .redirectUri(
     "http://127.0.0.1:9000/api/show-submission-form")
     .scope("writeIngredients")
     .scope("deleteIngredients")
     .scope(OidcScopes.OPENID)
     .clientSettings(org.springframework.security.oauth2.server.authorization.settings.ClientSettings.builder().requireAuthorizationConsent(true).build())
     .build();
     return new InMemoryRegisteredClientRepository(registeredClient);
    }



    @Bean
    public JWKSource<SecurityContext> jwkSource() {
    RSAKey rsaKey = generateRsa();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }
    private static RSAKey generateRsa() {
    KeyPair keyPair = generateRsaKey();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RSAKey.Builder(publicKey)
    .privateKey(privateKey)
    .keyID(UUID.randomUUID().toString())
    .build();
    }
    private static KeyPair generateRsaKey() {
    try {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    return keyPairGenerator.generateKeyPair();
    } catch (Exception e) {
    return null;
    }
    }
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        log.info("Creating JwtDecoder with JWK Source");
        JwtDecoder jwtDecoder = OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
        return new LoggingJwtDecoder(jwtDecoder);
    }


    
 
}