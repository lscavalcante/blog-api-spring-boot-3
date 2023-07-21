package com.lscavalcante.blog.configuration.security;

import com.lscavalcante.blog.configuration.exception.NotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    final UserDetailsService userDetailsService;
    final JwtService jwtService;
    final AntPathMatcher antPathMatcher;
    final UrlPathHelper urlPathHelper;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.antPathMatcher = new AntPathMatcher();
        this.urlPathHelper = new UrlPathHelper();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // get path
        String requestPath = urlPathHelper.getPathWithinApplication(request);
        if (!isPathMatch(requestPath, "/auth/**") && !isPathMatch(requestPath, "/api/v1/auth/**")) {
            if (!isRouteValid(requestPath)) {
                throw new NotFoundException("Route not found");
            }
        }
        String token = jwtService.resolveToken(request);
        if (token != null && jwtService.validJWT(token)) {
            Authentication auth = jwtService.getAuthentication(token);

            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPathMatch(String requestPath, String pattern) {
        return antPathMatcher.match(pattern, requestPath);
    }

    private boolean isRouteValid(String requestPath) {
        // Implemente a lógica para verificar se a rota existe ou qualquer outra validação necessária
        // Retorne true se a rota for válida, false caso contrário
        return true;
    }
}
