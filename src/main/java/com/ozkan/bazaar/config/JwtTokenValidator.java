package com.ozkan.bazaar.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    // Paths to skip JWT validation (login, register, WebSocket handshake)
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/auth/signin",
            "/auth/signup",
            "/ws",
            "/topic"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip validation for excluded paths
        for (String excludedPath : EXCLUDED_PATHS) {
            if (path.startsWith(excludedPath)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String jwt = request.getHeader("Authorization");

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            try {
                SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                e.printStackTrace(); // or log.error(...)

                throw new BadCredentialsException("Invalid JWT Token: "+ e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
