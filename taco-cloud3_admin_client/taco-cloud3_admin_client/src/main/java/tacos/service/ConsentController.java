package tacos.service;

import java.net.URLEncoder;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConsentController {
	
	private boolean isOAuth2Authentication(Authentication authentication) {
	    return authentication.getPrincipal() instanceof OAuth2User;
	}
	/*@GetMapping("/trigger-second-chain")
	public String showConsentPage(Authentication authentication) {
	    if (isOAuth2Authentication(authentication)) {
	        // Redirect to SecondChainController for OAuth2 logins
	        return "redirect:/trigger-second-chain";
	    } else {
	        // Show the consent page for traditional form logins
	        return "consent";
	    }
	}*/

    @PostMapping("/process-consent")
    public String processConsent(@RequestParam String scope, @RequestParam String consent) {
        if ("approve".equals(consent)) {
            // Process approval
            String redirectUri = "http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=taco-admin-client&scope=" + scope;
            return "redirect:" + redirectUri;
        } else {
            // Process denial
            return "redirect:/access-denied";
        }
    }
	@GetMapping("/trigger-second-chain")
	public String triggerOAuth2Flow() {
	    // Redirect directly to the OAuth2 authorization server
	    String redirectUri = "http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=taco-admin-client&scope=writeIngredients%20deleteIngredients";
	    return "redirect:" + redirectUri;
	}

	
	}

	
