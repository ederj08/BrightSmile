package com.brightsmile.backend.auth;

//Clase que representa la respuesta del login
//El servidor devuelve:{ "token": "eyJhbGci.."}


public class AuthResponse {

    private String token;

    //Constructor que recibe el token generado

    public AuthResponse(String token){
        this.token=token;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token=token;
    }
}
