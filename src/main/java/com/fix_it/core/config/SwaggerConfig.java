package com.fix_it.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Fixit")
                        .version("1.0")
                        .description("""
                            ### Como usar:
                            
                            1. Execute o comando para obter o token:
```bash
                            curl -X POST http://localhost:8085/realms/fixit/protocol/openid-connect/token \\
                              -H 'Content-Type: application/x-www-form-urlencoded' \\
                              -d 'client_id=fixit_backend' \\
                              -d 'username=user' \\
                              -d 'password=123' \\
                              -d 'grant_type=password'
```
                            
                            2. Copie o access_token da resposta
                            3. Clique no botão 'Authorize' e cole o token
                            """))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
