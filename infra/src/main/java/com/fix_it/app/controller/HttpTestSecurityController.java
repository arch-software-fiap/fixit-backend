package com.fix_it.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpTestSecurityController {

    @GetMapping("/public/endpoint")
    public ResponseEntity<String> publicEndpoint(){
        return ResponseEntity.status(HttpStatus.OK).body("Public Endpoint Called");
    }

    @GetMapping("/security/endpoint")
    public ResponseEntity<String> securityEndpoint(){
        return ResponseEntity.status(HttpStatus.OK).body("Security Endpoint Called");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/security/endpoint/admin")
    public ResponseEntity<String> securityEndpointWithAdminRolw(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.OK)
                .body("Security Endpoint With Role ADMIN Called" + jwt.getClaimAsString("preferred_username"));
    }

}
