package tacos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tacos.service.ConsentService;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

    @Autowired
    private ConsentService consentService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                // Log session ID or other details
                logger.info("Logging out session: " + session.getId());

                // Perform any necessary session cleanup
                session.invalidate();
            }

            // Assuming the username is the principal
            String username = authentication.getName();
            consentService.clearConsent(username);
        }
    }
}
