package pl.sginko.travelexpense.logic.user.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.sginko.travelexpense.logic.user.exception.UserException;

public class AuthenticationUtil {

    //    public static String getCurrentUserEmail() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//
//        if (principal instanceof User) {
//            User user = (User) principal;
//            return user.getUsername();
//        } else if (authentication instanceof OAuth2AuthenticationToken) {
//            OAuth2User oauthUser = (OAuth2User) principal;
//            String email = (String) oauthUser.getAttributes().get("email");
//            if (email == null) {
//                throw new UserException("Failed to retrieve the user's email from OAuth2 authentication attributes.");
//            }
//            return email;
//        } else if (principal instanceof String) {
//            if (principal.equals("anonymousUser")) {
//                throw new UserException("User is not authenticated. Please log in to create a travel expense.");
//            }
//            return (String) principal;
//        } else {
//            throw new UserException("Failed to retrieve the user's email from Principal. Principal type: " + principal.getClass().getName());
//        }
//    }

    public static String getCurrentUserEmail() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            return handleUserPrincipal((User) principal);
        } else if (authentication instanceof OAuth2AuthenticationToken && principal instanceof OAuth2User) {
            return handleOAuth2Authentication((OAuth2User) principal);
        } else if (principal instanceof String) {
            return handleStringPrincipal((String) principal);
        } else {
            return handleUnknownPrincipal(principal);
        }
    }

    private static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UserException("No authentication found in the security context.");
        }
        return authentication;
    }

    private static String handleUserPrincipal(User user) {
        return user.getUsername();
    }

    private static String handleOAuth2Authentication(OAuth2User oauthUser) {
        String email = (String) oauthUser.getAttributes().get("email");
        if (email == null) {
            throw new UserException("Failed to retrieve the user's email from OAuth2 authentication attributes.");
        }
        return email;
    }

    private static String handleStringPrincipal(String principal) {
        if ("anonymousUser".equals(principal)) {
            throw new UserException("User is not authenticated. Please log in to create a travel expense.");
        }
        return principal;
    }

    private static String handleUnknownPrincipal(Object principal) {
        throw new UserException("Failed to retrieve the user's email from Principal. Principal type: " + principal.getClass().getName());
    }
}
