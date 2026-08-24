package com.brightsmile.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//Habilita WebSocket con el protocolo STOMP
//STOMP es un protocolo de mensajería sobre WebSocket
//Es como HTTP pero para comuniación bidireccional en tiempo real

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /topic-> prefijo para mensajes que van a todos los suscriptores
        // Es como un canal de broadcast
        config.enableSimpleBroker("/topic");

       //app->prefijo para mensajes que van al servidor
       //Cuando Angular manda un mensaje, empieza con /app/..
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // /ws-> la URl donde angular se conecta al WebSocket
        //Angular hará: new SockJs('http://localhost:8080/ws')
        //withSockJS()->fallback para navegadores que no soportan WebSocket nativo

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

}
