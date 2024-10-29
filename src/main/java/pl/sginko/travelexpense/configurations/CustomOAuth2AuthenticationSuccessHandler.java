package pl.sginko.travelexpense.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.logic.auth.service.userService.UserService;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("given_name");
        String surname = oAuth2User.getAttribute("family_name");

        userService.registerOAuth2User(email, name, surname);

        response.sendRedirect("/pages/main.html");
    }
}