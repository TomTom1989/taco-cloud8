package tacos.data;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tacos.AppUser;

@Service
public class AppUserService {

    public static Object appUserService;

	public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof AppUser) {
                AppUser currentUser = (AppUser) principal;
                return currentUser.getId();
            }
        }
        return null; // Or handle it differently if user is not found
    }
}

