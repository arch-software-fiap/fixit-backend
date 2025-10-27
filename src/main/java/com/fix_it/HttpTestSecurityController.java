package com.fix_it;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
