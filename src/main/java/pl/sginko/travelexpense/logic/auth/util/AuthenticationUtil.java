package pl.sginko.travelexpense.logic.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.sginko.travelexpense.logic.auth.exception.UserException;

public class AuthenticationUtil {

    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            User user = (User) principal;
            return user.getUsername();
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oauthUser = (OAuth2User) principal;
            String email = (String) oauthUser.getAttributes().get("email");
            if (email == null) {
                throw new UserException("Failed to retrieve the user's email from OAuth2 authentication attributes.");
            }
            return email;
        } else if (principal instanceof String) {
            if (principal.equals("anonymousUser")) {
                throw new UserException("User is not authenticated. Please log in to create a travel expense.");
            }
            return (String) principal;
        } else {
            throw new UserException("Failed to retrieve the user's email from Principal. Principal type: " + principal.getClass().getName());
        }
    }
}
