    package com.fix_it.core.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.convert.converter.Converter;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.oauth2.jwt.Jwt;
    import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
    import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
    import org.springframework.security.web.SecurityFilterChain;

    import java.util.Collection;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;
    import java.util.stream.Stream;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/public/**").permitAll()
                            .requestMatchers("/actuator/**").permitAll()
                            .requestMatchers("/api/v1/public/**").permitAll()
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    )
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .csrf(AbstractHttpConfigurer::disable)
                    .build();
        }

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {
            JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
            converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
            return converter;
        }

        @Bean
        public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
            JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

            return jwt -> {
                Collection<GrantedAuthority> authorities = defaultConverter.convert(jwt);

                Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                if (realmAccess == null) {
                    return authorities;
                }

                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) realmAccess.get("roles");

                if (roles == null || roles.isEmpty()) {
                    return authorities;
                }

                Collection<GrantedAuthority> keycloakAuthorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                        .collect(Collectors.toList());

                return Stream.concat(authorities.stream(), keycloakAuthorities.stream())
                        .collect(Collectors.toSet());
            };
        }
    }
