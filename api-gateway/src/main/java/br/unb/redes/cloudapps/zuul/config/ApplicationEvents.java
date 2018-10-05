package br.unb.redes.cloudapps.zuul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.unb.redes.cloudapps.zuul.security.KeycloakConfigure;

@Configuration
@Component
public class ApplicationEvents {
	
	@EventListener
    public void eventosAoIniciarAplicacao(ContextRefreshedEvent event) {
       //Configurar o keycloak
		KeycloakConfigure.getInstance().configurarKeycloak();
    }
}
