package pl.books.magagement.config;

import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.function.ServerRequest;
import pl.books.magagement.service.RevokedRefreshTokenService;
import pl.books.magagement.service.UserService;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException, IndexOutOfBoundsException, EntityNotFoundException
    {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null){

            handleAuthentication(authHeader);
        }

        filterChain.doFilter(request, response);
    }

    private void handleAuthentication(String authHeader){

        String accessToken = authHeader.substring(6);

        jwtService.verifyToken(accessToken);

        String username = jwtService.getUsername(accessToken);
        UserDetails foundUser = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
            foundUser, null, foundUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
    }
}
