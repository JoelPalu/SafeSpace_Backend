/**
 * Configuration class for defining the security filter chain in the application.
 * Sets up authentication, session management, and authorization rules for HTTP requests.
 */
package G2.SafeSpace.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilter {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Defines the security filter chain for the application.
     * Configures CSRF protection, session management, authentication, and authorization rules.
     *
     * @param http the {@link HttpSecurity} object used to configure the filter chain.
     * @return a configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authConfig -> {
                    // Permit all users to access the following endpoints
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
                    authConfig.requestMatchers("/error").permitAll();

                    // Authentication for static resources
                    authConfig.requestMatchers("/**").authenticated();

                    // Authenticated users can access the following endpoints
                    authConfig.requestMatchers("api/v1/users").authenticated();
                    authConfig.requestMatchers("api/v1/users/**").authenticated();
                    authConfig.requestMatchers("api/v1/post/").authenticated();
                    authConfig.requestMatchers("api/v1/post/**").authenticated();
                    authConfig.requestMatchers("api/v1/storage/**").authenticated();
                    authConfig.requestMatchers("api/v1/message/**").authenticated();

                    // Permit all users to access the following endpoints for testing purposes
                    authConfig.requestMatchers("/posts/**").permitAll();
                    authConfig.requestMatchers("api/v1/events").permitAll();

                    // Deny access to any other requests
                    authConfig.anyRequest().denyAll();
                });
        return http.build();
    }
}
