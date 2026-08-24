package com.brightsmile.backend.auth;

//clase que represeenta el body del request de login
//El usuario manda:{"username": "admin","password:" : "admin123"}

public class AuthRequest {

    private String username;
    private String password;

    //Construcyor vacio obligatorio para que Jackson pueda deserializar el JSON

    public AuthRequest (){}

    public String getUsername(){ return username;}
    public void setUsername(String username){this.username=username;}

    public String getPassword(){return password;}
    public void setPassword(String password) {this.password=password;}

}
