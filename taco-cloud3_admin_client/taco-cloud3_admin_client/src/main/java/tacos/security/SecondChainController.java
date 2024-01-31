package tacos.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

@Controller
@RequestMapping("/facebook-trigger-second-chain")
public class SecondChainController {
	
	private boolean isOAuth2Authentication(Authentication authentication) {
	    return authentication instanceof OAuth2AuthenticationToken;
	}


    private final RegisteredClientRepository registeredClientRepository;
    private static final Logger logger = LoggerFactory.getLogger(SecondChainController.class);

    @Autowired
    public SecondChainController(RegisteredClientRepository registeredClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
    }

    @GetMapping
    public RedirectView redirectToOAuth2(Authentication authentication) {
        if (!isOAuth2Authentication(authentication)) {
            // Redirect or handle non-OAuth2 logins differently
            return new RedirectView("/error-custom"); // Adjust as needed
        }

        try {
            RegisteredClient registeredClient = registeredClientRepository.findByClientId("taco-admin-client");

            if (registeredClient == null) {
                logger.error("Registered client not found for ID: taco-admin-client");
                return new RedirectView("/error-custom");
            }

            String authorizationRequestUri = "http://localhost:9000/oauth2/authorize?response_type=code&client_id=" + registeredClient.getClientId() + "&scope=writeIngredients%20deleteIngredients";

            return new RedirectView(authorizationRequestUri);
        } catch (Exception e) {
            logger.error("An error occurred during redirection to OAuth2: ", e);
            return new RedirectView("/error-custom");
        }
    }
}
