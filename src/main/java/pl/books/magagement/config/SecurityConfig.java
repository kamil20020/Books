package pl.books.magagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import pl.books.magagement.repository.UserRepository;
import pl.books.magagement.service.UserService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/publishers/**", "/books/**", "/authors/**").permitAll()
                .requestMatchers("/users/**", "/roles/**").hasRole("ADMIN")
                .requestMatchers("/swagger-ui/**").hasRole("ADMIN")
                .requestMatchers("/security/public").permitAll()
                .requestMatchers("/security/requires-admin").hasRole("ADMIN")
                .anyRequest().authenticated()
            );

        http
            .cors(cors -> cors.configurationSource(request ->  {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:80", "http://localhost"));
                configuration.setAllowedMethods(List.of("*"));
                configuration.setAllowedHeaders(List.of("*"));

                return configuration;
            }));

        return http.build();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(){

        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();

        daoAuthProvider.setPasswordEncoder(passwordEncoder());
        daoAuthProvider.setUserDetailsService(userService);

        return daoAuthProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
}
