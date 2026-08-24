package com.brightsmile.backend.auth;

import com.brightsmile.backend.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

//Controller que maneja el login
//Ruta base:/auth

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.jwtUtil=jwtUtil;
    }

    //POST / auth/login
    //Recibe username and password, devuelve token JWT

    @PostMapping ("/login")
    public AuthResponse login(@RequestBody AuthRequest request){
        //Crea el objeto de autenticación con username y password
        //Spring Security verifica las credenciales contra el UserdatailsService
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        //Extrae los roles del usuario autenticado
        //Ejemplo: ["ROLE_ADMIN"]
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        //Genera el token JWT con username y roles
        String token =jwtUtil.generateToken(authentication.getName(),roles);

        // Devuelve el token al frontend
        return new AuthResponse(token);
    }

}
