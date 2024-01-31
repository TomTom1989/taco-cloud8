package tacos.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tacos.data.AppUserService;

@RestController
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/log-user-id")
    public String logUserId() {
        Long userId = appUserService.getCurrentUserId();
        if (userId != null) {
            String message = "User ID " + userId + " logged in the console";
            System.out.println(message);
            return message;
        } else {
            return "No authenticated user found";
        }
    }
}
