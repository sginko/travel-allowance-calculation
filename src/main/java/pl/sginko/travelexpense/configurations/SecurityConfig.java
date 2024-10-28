package pl.sginko.travelexpense.configurations;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.security.config.Customizer.withDefaults;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.addAllowedOrigin("http://localhost:63342");
                    config.addAllowedOrigin("http://localhost:8080");
                    config.addAllowedHeader("*");
                    config.addAllowedMethod("*");
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/pages/login.html", "/pages/register.html", "/styles/**",
                                "/javascript/**", "/api/v1/travels/new-user", "/api/v1/travels/new-travel").permitAll()
                        .requestMatchers("/api/v1/travels//{email}/change-role",
                                "/api/v1/travels/all-users").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                                .loginPage("/pages/login.html")
                                .loginProcessingUrl("/login")
//                                .usernameParameter("email")
//                                .defaultSuccessUrl("/static/pages/main.html", true)
                                .successHandler(myAuthenticationSuccessHandler())
                                .failureUrl("/pages/login.html?error=true")
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index.html")
                        .permitAll()
                )
                .oauth2Login(oauth2LoginConfigurer -> oauth2LoginConfigurer.successHandler(customOAuth2AuthenticationSuccessHandler))

//                .formLogin(withDefaults())
                .httpBasic(withDefaults());

//        // Настройка Remember Me
//        http.rememberMe(rememberMeConfigurer ->
//                rememberMeConfigurer
//                        .tokenValiditySeconds(86400)
//                        .rememberMeParameter("remember-me")
//        );

//        // Настройка сессии
//        http.sessionManagement(sessionManagementConfigurer ->
//                sessionManagementConfigurer
//                        .sessionFixation(sessionFixationConfigurer ->
//                                sessionFixationConfigurer.migrateSession()
//                        )
//        );
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            RequestCache requestCache = new HttpSessionRequestCache();
            SavedRequest savedRequest = requestCache.getRequest(request, response);

            if (savedRequest != null) {
                String targetUrl = savedRequest.getRedirectUrl();
                response.sendRedirect(targetUrl);
            } else {
                response.sendRedirect("/pages/main.html");
            }
        };
    }

//    @Bean
//    public AuthenticationSuccessHandler oAuth2SuccessHandler() {
//        return (request, response, authentication) -> {
//            var oAuth2User = (OAuth2User) authentication.getPrincipal();
//            String email = oAuth2User.getAttribute("email");
//            String name = oAuth2User.getAttribute("name");
//            String surname = oAuth2User.getAttribute("family_name");
//
//            userService.registerOAuth2User(email, name, surname);
//
//            response.sendRedirect("/pages/main.html");
//        };
//    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("http://localhost:63342");
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/static/**", "/styles/**", "/js/**", "/pages/**")
//                .requestMatchers(HttpMethod.OPTIONS, "/**");
//    }
}
