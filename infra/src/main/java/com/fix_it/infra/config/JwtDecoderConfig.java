package com.fix_it.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtDecoderConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String keycloakServerUrl;

    @Value("${security.jwt.shared-secret:${security.jwt.lambda-secret:}}")
    private String externalJwtSharedSecret;

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder keycloakDecoder = NimbusJwtDecoder.withJwkSetUri(keycloakServerUrl).build();
        keycloakDecoder.setJwtValidator(buildKeycloakValidator());

        JwtDecoder externalJwtDecoder = buildExternalJwtDecoder();
        if (externalJwtDecoder == null) {
            return keycloakDecoder;
        }

        return token -> {
            try {
                return externalJwtDecoder.decode(token);
            } catch (JwtException externalTokenException) {
                return keycloakDecoder.decode(token);
            }
        };
    }

    private OAuth2TokenValidator<Jwt> buildKeycloakValidator() {
        OAuth2TokenValidator<Jwt> withTimestamp = new JwtTimestampValidator();
        OAuth2TokenValidator<Jwt> withAudience =
                new JwtClaimValidator<List<String>>("aud", aud -> aud != null && !aud.isEmpty());

        return new DelegatingOAuth2TokenValidator<>(withTimestamp, withAudience);
    }

    private JwtDecoder buildExternalJwtDecoder() {
        if (!StringUtils.hasText(externalJwtSharedSecret)) {
            return null;
        }

        SecretKey secretKey = new SecretKeySpec(externalJwtSharedSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        NimbusJwtDecoder externalJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
        externalJwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(),
                new JwtClaimValidator<String>("sub", StringUtils::hasText)
        ));
        return externalJwtDecoder;
    }
}
