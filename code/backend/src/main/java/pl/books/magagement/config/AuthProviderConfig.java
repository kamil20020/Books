package pl.books.magagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthProviderConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(){

        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();

        daoAuthProvider.setPasswordEncoder(passwordEncoder);
        daoAuthProvider.setUserDetailsService(userDetailsService);

        return daoAuthProvider;
    }
}
