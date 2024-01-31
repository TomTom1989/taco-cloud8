package tacos.security;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class OAuth2Controller {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);

    @GetMapping("/my-oauth2-endpoint")
    public String redirectToOAuth2() {
    	logger.info("Redirecting to Facebook OAuth2 endpoint.");
        return "redirect:/oauth2/authorization/facebook";
    }
    

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error,
                               HttpServletRequest request,
                               Model model) {
        if (error != null) {
            // Attempt to retrieve error details from the request parameters or attributes
            String errorMessage = (String) request.getAttribute("error.message");
            errorMessage = errorMessage != null ? errorMessage : "An unspecified error occurred during login.";
            
            // Log the error message
            logger.error("There was an error during the login process: " + errorMessage);

            // Add the error message to the model to display it in the view
            model.addAttribute("errorMessage", errorMessage);
        }
        return "login"; // Return to the login view
    }
    
    
    @GetMapping("/oauth2/code/taco-admin-client")
    public String handleOAuth2Code(@RequestParam String code) {
        // Additional logic can be added here if needed
        return "redirect:/api/show-submission-form?code=" + code;
    }

    
    
    
}
