package com.immpresariat.ArtAgencyApp.auth;


import com.immpresariat.ArtAgencyApp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request,
            @RequestParam(value="role", defaultValue = "user", required = false) String role) {
    return  ResponseEntity.ok(service.register(request, role));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request) {
        return  ResponseEntity.ok(service.authenticate(request));
    }


}
