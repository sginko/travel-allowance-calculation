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
    void shouldReturnEmailFromUserPrincipal() {
        // GIVEN
        User user = new User("user@example.com", "password", List.of());
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(user, null));

        // WHEN
        String email = AuthenticationUtil.getCurrentUserEmail();

        // THEN
        assertThat(email).isEqualTo("user@example.com");
    }

    @Test
    void shouldReturnEmailFromOAuth2UserPrincipal() {
        // GIVEN
        OAuth2User oauthUser = mock(OAuth2User.class);
        when(oauthUser.getAttributes()).thenReturn(Map.of("email", "oauth@example.com"));
        OAuth2AuthenticationToken authToken = new OAuth2AuthenticationToken(oauthUser, List.of(), "google");
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // WHEN
        String email = AuthenticationUtil.getCurrentUserEmail();

        // THEN
        assertThat(email).isEqualTo("oauth@example.com");
    }

    @Test
    void shouldThrowExceptionForAnonymousUser() {
        // GIVEN
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("anonymousUser", null));

        // WHEN & THEN
        assertThrows(UserException.class, () -> AuthenticationUtil.getCurrentUserEmail());
    }

    @Test
    void shouldThrowExceptionIfEmailNotPresentInOAuth2UserAttributes() {
        // GIVEN
        OAuth2User oauthUser = mock(OAuth2User.class);
        when(oauthUser.getAttributes()).thenReturn(Map.of());
        OAuth2AuthenticationToken authToken = new OAuth2AuthenticationToken(oauthUser, List.of(), "google");
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // WHEN & THEN
        assertThrows(UserException.class, () -> AuthenticationUtil.getCurrentUserEmail());
    }

    @Test
    void shouldThrowExceptionIfAuthenticationIsNull() {
        // GIVEN
        SecurityContextHolder.clearContext();

        // WHEN & THEN
        assertThrows(UserException.class, () -> AuthenticationUtil.getCurrentUserEmail());
    }

    @Test
    void shouldThrowExceptionForUnknownPrincipalType() {
        // GIVEN
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(new Object(), null));

        // WHEN & THEN
        assertThrows(UserException.class, () -> AuthenticationUtil.getCurrentUserEmail());
    }
}
