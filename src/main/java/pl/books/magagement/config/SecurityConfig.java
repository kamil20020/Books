package pl.books.magagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import pl.books.magagement.repository.UserRepository;
import pl.books.magagement.service.UserService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
//            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(request -> request
                .requestMatchers("/security/public").permitAll()
                .requestMatchers(HttpMethod.GET, "/publishers/**", "/books/**", "/authors/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users", "/users/login", "/users/refresh-access-token").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/logout").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/books/**").authenticated()
                .requestMatchers("/security/requires-admin").hasRole("ADMIN")
                .requestMatchers("/users/**", "/roles/**").hasRole("ADMIN")
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() //.hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler(customAccessDeniedHandler)
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
}
