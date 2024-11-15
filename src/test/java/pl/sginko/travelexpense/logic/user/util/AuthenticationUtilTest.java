package pl.sginko.travelexpense.logic.user.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pl.sginko.travelexpense.logic.user.exception.UserException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationUtilTest {
    @BeforeEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void should_return_email_from_user_principal() {
        // GIVEN
        User user = new User("user@test.com", "password", List.of());
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(user, null));

        // WHEN
        String email = AuthenticationUtil.getCurrentUserEmail();

        // THEN
        assertThat(email).isEqualTo("user@test.com");
    }

    @Test
    void should_return_email_from_OAuth2User_principal() {
        // GIVEN
        OAuth2User oauthUser = mock(OAuth2User.class);
        when(oauthUser.getAttributes()).thenReturn(Map.of("email", "oauth@test.com"));
        OAuth2AuthenticationToken authToken = new OAuth2AuthenticationToken(oauthUser, List.of(), "google");
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // WHEN
        String email = AuthenticationUtil.getCurrentUserEmail();

        // THEN
        assertThat(email).isEqualTo("oauth@test.com");
    }

    @Test
    void should_throw_exception_for_anonymous_user() {
        // GIVEN
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("anonymousUser", null));

        // WHEN & THEN
        assertThrows(UserException.class, () -> AuthenticationUtil.getCurrentUserEmail());
    }

    @Test
    void should_throw_exception_if_email_not_present_in_OAuth2User_attributes() {
        // GIVEN
        OAuth2User oauthUser = mock(OAuth2User.class);
        when(oauthUser.getAttributes()).thenReturn(Map.of());
        OAuth2AuthenticationToken authToken = new OAuth2AuthenticationToken(oauthUser, List.of(), "google");
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // WHEN & THEN
        assertThrows(UserException.class, () -> AuthenticationUtil.getCurrentUserEmail());
    }

    @Test
    void should_throw_exception_if_authentication_is_null() {
        // GIVEN
        SecurityContextHolder.clearContext();

        // WHEN & THEN
        assertThrows(UserException.class, () -> AuthenticationUtil.getCurrentUserEmail());
    }

    @Test
    void should_throw_exception_for_unknown_principal_type() {
        // GIVEN
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(new Object(), null));

        // WHEN & THEN
        assertThrows(UserException.class, () -> AuthenticationUtil.getCurrentUserEmail());
    }
}
