package br.unb.redes.cloudapps.zuul.security;

import java.io.Serializable;

public class AuthRequestModel implements Serializable {

    private static final long serialVersionUID = -8445323548965154778L;

    private String username;
    private String password;

    public AuthRequestModel() {
        super();
    }

    public AuthRequestModel(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
