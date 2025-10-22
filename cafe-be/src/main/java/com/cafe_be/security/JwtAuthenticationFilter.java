package com.cafe_be.security;

import com.cafe_be.service.TokenService;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends HttpFilter {

    private final TokenService tokenService;

    public JwtAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                DecodedJWT jwt = tokenService.verify(token);
                String username = jwt.getSubject();
                // read roles claim (array of strings)
                List<String> roles = jwt.getClaim("roles").asList(String.class);
                List<SimpleGrantedAuthority> authorities = roles == null ? List.of() :
                        roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toList());

                // build Authentication and set into SecurityContext
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                // attach some useful details
                auth.setDetails(java.util.Map.of(
                        "id", jwt.getClaim("id").asLong(),
                        "email", jwt.getClaim("email").asString(),
                        "fullName", jwt.getClaim("fullName").asString()
                ));
                SecurityContextHolder.getContext().setAuthentication(auth);
                req.setAttribute("principal", username);
            } catch (Exception ex) {
                // invalid token - ignore and proceed as anonymous
            }
        }
        chain.doFilter(req, res);
    }
}
