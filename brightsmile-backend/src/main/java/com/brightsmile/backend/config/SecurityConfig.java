package com.brightsmile.backend.config;

import org.springframework.http.HttpMethod;
import com.brightsmile.backend.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
//Habilita el uso de @PreAuthorize en los controllers
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public  SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception{
        http
                //Desactivamos CSRF porque usamos JWT, no sesiones
                .csrf(csrf->csrf.disable())
                //Activa CORS con la configuración de abajo
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth->auth
                //Rutas públicas -no requieren token
                        .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/ws/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/citas").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/citas/disponibilidad").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/citas/buscar").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/servicios/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/horarios/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/citas/*/cancelar").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/citas/completar-pasadas").permitAll()
                        //Rutas de Swagger-documentación pública
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs/**"
                                ).permitAll()
                        //Cualquier otra ruta requiere autenticación
                                .anyRequest().authenticated()
                )
                //Agrega el filtro JWT antes del filtro de autenticación estándar
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //Configuración CORS - permite peticiones desde Angular en localhost:4200
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }
    //Expone el Authenticationmanager como bean para usarlo en el login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //Define los usuarios del sistema en memoria
    @Bean
    public UserDetailsService userDetailsService(){

        //Usuario administrador de la clinica - acceso total
        UserDetails admin =User
                .withUsername("admin")
                .password("{noop}admin123")
                .roles("ADMIN")
                .build();
        //Recepcionista - puede ver y gestionar citas
        UserDetails recepcionista = User
                .withUsername("recepcionista")
                .password("{noop}recepcionista123")
                .roles("RECEP")
                .build();
        //Dentista - puede ver sus citas del día
        UserDetails dentista = User
                .withUsername("dentista")
                .password("{noop}dentista123")
                .roles("DENTISTA").build();

        return new InMemoryUserDetailsManager(admin,recepcionista,dentista);
    }
}
