package com.fix_it.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtDecoderConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String KEYCLOAK_SERVER_URL;

    @Bean
    public JwtDecoder jwtDecoder() {

        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(KEYCLOAK_SERVER_URL).build();

        OAuth2TokenValidator<Jwt> withTimestamp = new JwtTimestampValidator();
        OAuth2TokenValidator<Jwt> withAudience =
                new JwtClaimValidator<List<String>>("aud", aud -> aud != null && !aud.isEmpty());

        OAuth2TokenValidator<Jwt> withoutIssuerValidation =
                new DelegatingOAuth2TokenValidator<>(withTimestamp, withAudience);

        decoder.setJwtValidator(withoutIssuerValidation);

        return decoder;
    }
}
