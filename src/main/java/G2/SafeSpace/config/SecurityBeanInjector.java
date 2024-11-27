/**
 * Configuration class to provide security-related beans for the application.
 * Handles the setup of authentication manager, authentication provider,
 * password encoder, and user details service.
 */
package G2.SafeSpace.config;

import G2.SafeSpace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityBeanInjector {

    @Autowired
    private UserRepository userRepository;

    /**
     * Provides an {@link AuthenticationManager} bean.
     *
     * @param authenticationConfiguration the {@link AuthenticationConfiguration} used to configure the manager.
     * @return the configured {@link AuthenticationManager}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); //providerManager implements the authentication manager interface
    }

    /**
     * Provides an {@link AuthenticationProvider} bean configured with a {@link DaoAuthenticationProvider}.
     *
     * @return the configured {@link AuthenticationProvider}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /**
     * Provides a {@link PasswordEncoder} bean using the {@link BCryptPasswordEncoder}.
     *
     * @return a {@link PasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides a {@link UserDetailsService} bean to load user details by username.
     *
     * @return a {@link UserDetailsService} instance that retrieves user details from the {@link UserRepository}.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username);
    }
}
