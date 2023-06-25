package com.lscavalcante.blog.configuration.security;

//public class SecurityConfiguration {
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // if there's no exception inside method JwtAuthenticationFilter but the route is not authenticated
//    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    // if there's any exception inside method JwtAuthenticationFilter call chainExceptionHandlerFilter
    private final ChainExceptionHandlerFilter chainExceptionHandlerFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter,
                                 AuthenticationProvider authenticationProvider,
//                                 JwtAuthenticationEntryPoint authenticationEntryPoint,
                                 ChainExceptionHandlerFilter chainExceptionHandlerFilter
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
//        this.logoutHandler = logoutHandler;
//        this.authenticationEntryPoint = authenticationEntryPoint;
        this.chainExceptionHandlerFilter = chainExceptionHandlerFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(chainExceptionHandlerFilter, LogoutFilter.class)
                .authorizeHttpRequests((e) -> {
                    e.requestMatchers(
                                    "/api/auth/**",
                                    "/v2/api-docs",
                                    "/v3/api-docs",
                                    "/v3/api-docs/**",
                                    "/swagger-resources",
                                    "/swagger-resources/**",
                                    "/configuration/ui",
                                    "/configuration/security",
                                    "/swagger-ui/**",
                                    "/webjars/**",
                                    "/swagger-ui.html"
                            ).permitAll()
//                            .requestMatchers(HttpMethod.GET,"/user/**").hasAnyAuthority("admin:create")
                            .requestMatchers(HttpMethod.GET, "/user/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/user/**").authenticated()
                            .requestMatchers("/auth/**").permitAll()
                            .anyRequest().permitAll()
                    ;


                })
                .sessionManagement((e) -> {
                    e.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((e) -> {
//                    e.logoutUrl("/api/v1/auth/logout");
//                    e.addLogoutHandler(logoutHandler);
//                    e.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
                })
                .exceptionHandling((e) -> {
//                    e.authenticationEntryPoint(authenticationEntryPoint);
                })
        ;

        return http.build();
    }
}


//                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())


//                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
//                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
//                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
//                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())


                            /* .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())

                             .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
                             .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
                             .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
                             .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())*/
//                            .anyRequest()
//                            .authenticated();