package com.security.V9_Marzo_25.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.security.V9_Marzo_25.model.LoginRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Define el prefijo para las rutas de autenticación
public class AuthController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String AUTH0_DOMAIN = "https://dev-hn6ws7o3ayfpoqpa.eu.auth0.com/";
    private final String CLIENT_ID = "9uPAyIP7nQuyolzea0KLQ0XyDVy0t3QN";
    private final String CLIENT_SECRET = "uSm9YbzzoOxFD3fPx1PwnC-fqchXoFOhT1nRLZtIM-rSn_n4XB2d6L0mUAtNDecj";
    

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Configurar la petición a Auth0
        String url = "https://dev-hn6ws7o3ayfpoqpa.eu.auth0.com/oauth/token";
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "password");
        body.put("username", request.getEmail());
        body.put("password", request.getPassword());
        body.put("audience", "https://dev-hn6ws7o3ayfpoqpa.eu.auth0.com/api/v2/");
        body.put("client_id", "9uPAyIP7nQuyolzea0KLQ0XyDVy0t3QN");
        body.put("client_secret", "uSm9YbzzoOxFD3fPx1PwnC-fqchXoFOhT1nRLZtIM-rSn_n4XB2d6L0mUAtNDecj");

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String auth0Token = response.getBody().get("access_token").toString();
                return ResponseEntity.ok(Map.of("auth0_token", auth0Token));
            } else {
                System.out.println("Auth0 Error: " + response.getBody()); // IMPRIME EL ERROR AQUÍ
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace(); // IMPRIME CUALQUIER ERROR EXCEPCIONAL
            return ResponseEntity.status(500).body("Error en la autenticación");
        }
        
        
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No refresh token provided");
        }

        // Pedimos un nuevo access token a Auth0
        String url = AUTH0_DOMAIN + "oauth/token";
        Map<String, String> payload = new HashMap<>();
        payload.put("grant_type", "refresh_token");
        payload.put("client_id", CLIENT_ID);
        payload.put("client_secret", CLIENT_SECRET);
        payload.put("refresh_token", refreshToken);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, payload, Map.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        // Devolvemos el nuevo access token a Angular
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint() {
        return ResponseEntity.ok(Map.of("message", "Acceso permitido: API protegida funciona correctamente"));
    }

   
}

