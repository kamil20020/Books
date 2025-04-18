package pl.books.magagement.config;

import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.function.ServerRequest;
import pl.books.magagement.service.RevokedRefreshTokenService;
import pl.books.magagement.service.UserService;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException, IndexOutOfBoundsException, EntityNotFoundException
    {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null){

            try {
                handleAuthentication(authHeader);
            }
            catch(IllegalArgumentException e){

                log.error(e.getMessage());

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleAuthentication(String authHeader) throws IllegalArgumentException, UsernameNotFoundException {

        String accessToken = authHeader.substring(6);

        jwtService.verifyToken(accessToken);

        String username = jwtService.getUsername(accessToken);
        UserDetails foundUser = userService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
            foundUser, null, foundUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
    }
}
