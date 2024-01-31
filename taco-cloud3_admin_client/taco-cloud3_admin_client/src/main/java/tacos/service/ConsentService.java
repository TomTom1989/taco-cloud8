package tacos.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Service
public class ConsentService {
    private static final Logger logger = LoggerFactory.getLogger(ConsentService.class);

    private Map<String, Set<String>> userConsents = new HashMap<>();

    // Record consent for traditional username/password authentication
    public void recordConsent(String username, Set<String> scopes) {
        userConsents.put(username, scopes);
        logger.info("Consent recorded for user '{}': {}", username, scopes);
    }

    // Check consent for both traditional and OAuth2 authentication
    public boolean checkConsent(String username, Set<String> scopes, OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            // For OAuth2 users, check the scopes in the OAuth2User attributes
            Set<String> oauthScopes = oAuth2User.getAuthorities().stream()
                                                .map(authority -> authority.getAuthority())
                                                .collect(Collectors.toSet());
            boolean hasConsent = oauthScopes.containsAll(scopes);
            logger.info("OAuth2 consent check for user '{}'. Required: {}. Granted: {}. Result: {}", username, scopes, oauthScopes, hasConsent);
            return hasConsent;
        } else {
            // For traditional users, check the consent from the stored records
            Set<String> grantedScopes = userConsents.getOrDefault(username, Collections.emptySet());
            boolean hasConsent = grantedScopes.containsAll(scopes);
            logger.info("Traditional consent check for user '{}'. Required: {}. Granted: {}. Result: {}", username, scopes, grantedScopes, hasConsent);
            return hasConsent;
        }
    }

    public void clearConsent(String username) {
        userConsents.remove(username);
        logger.info("Consent cleared for user '{}'", username);
    }
}
