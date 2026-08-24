package com.brightsmile.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.security.Security;
import java.util.List;
import java.util.stream.Collectors;

//Este filtro intercepta cada petición Http antes de que llegue al controller
//Su trabajo es leer el token JWT del header, validarlo y autenticar al usuario

@Component


public class JwtFilter extends OncePerRequestFilter{

    private final JwtUtil jwtUtil;

    //Spring inyecta JwUtil automaticamente por constructor
    public JwtFilter(JwtUtil jwtUtil){
        this.jwtUtil=jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        //Lee el header "Autorización" de la petición Http
        String authHeader = request.getHeader("Authorization");

        //si no hay header o no empieza con "Bearer", deja pasar la petición
        //Esto permite que /auth/login sea accesible sin token
        if (authHeader==null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        //Extrae solo el token quitando el prefijo "Bearer"
        String token = authHeader.substring(7);

        //Verifica que el token sea válido y no haya expirado
        if (jwtUtil.isTokenValid(token)){

            //extrae el username del token
            String username = jwtUtil.extractUsername(token);

            //Extrae los roles y los convierte al formato que Spring Security entiende
            List<SimpleGrantedAuthority>authorities = jwtUtil.extractRoles(token)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            //Crea el objeto de autenticación con username y roles
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null,authorities);

            //Registra al usuario como autenticado en el contexto de seguridad
            //A partir de aqui Spring Security sabe quien es el usuario

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //Deja continuar la petición hacia el controller
        filterChain.doFilter(request, response);
    }
}
