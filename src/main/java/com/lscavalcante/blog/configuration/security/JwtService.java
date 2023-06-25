package com.lscavalcante.blog.configuration.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lscavalcante.blog.configuration.exception.InvalidJwtAuthenticationException;
import com.lscavalcante.blog.dto.token.ResponseToken;
import com.lscavalcante.blog.model.user.Role;
import com.lscavalcante.blog.model.user.User;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtService {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.secret-key.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    public JwtService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public ResponseToken createJWT(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        String accessToken = getAccessToken(user.getUsername(), now, validity, roles);
        String refreshToken = getRefreshToken(user.getUsername(), now, roles);
        return new ResponseToken(user.getUsername(), true, now, validity, accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        System.out.println(decodedJWT.getSubject());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return null;
        }
        return bearerToken.substring("Bearer ".length());
    }

    public boolean validJWT(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            DecodedJWT decodedJWT = decodedToken(token);
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception ex) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }

    private String getRefreshToken(String username, Date now, List<String> roles) {
        Date validityRefreshToken = new Date(now.getTime() + (validityInMilliseconds * 3)); // 3h

        return JWT.create()
                .withIssuedAt(now)
                .withClaim("roles", roles)
                .withExpiresAt(validityRefreshToken)
                .withSubject(username)
                .sign(algorithm)
                .strip();
    }

    private String getAccessToken(String username, Date now, Date validity, List<String> roles) {
        String issueUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();


        return JWT.create()
                .withIssuedAt(now)
                .withClaim("roles", roles)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issueUrl)
                .sign(algorithm)
                .strip();
    }

    private DecodedJWT decodedToken(String token) {
        var alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        return verifier.verify(token);
    }
}
