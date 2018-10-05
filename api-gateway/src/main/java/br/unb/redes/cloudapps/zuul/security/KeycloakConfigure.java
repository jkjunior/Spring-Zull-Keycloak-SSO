package br.unb.redes.cloudapps.zuul.security;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.unb.redes.cloudapps.zuul.util.Constantes;

/**
 * Classe Singleton para configurar o Keycloak
 * @author joao.esper
 *
 */
public class KeycloakConfigure {
	private final String JSON_REALM_FIELD = "realm";
	private final String JSON_URL_FIELD = "auth-server-url";
	private final String JSON_CLIENT_ID_FIELD = "resource";
	private final String JSON_CREDENTIALS_FIELD = "credentials";
	private final String JSON_CLIENT_SECRET_FIELD = "secret";

	private String realmName;
	private String authServerUrl;
	private String clientId;
	private String clientSecret;
	
	private KeycloakConfigure() {
	}

	public void configurarKeycloak() {
		ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		byte[] jsonData;
		try {
			jsonData = Files.readAllBytes(
					Paths.get(getClass().getClassLoader().getResource(Constantes.KEYCLOAK_NOME_ARQUIVO_JSON).toURI()));
			objectMapper.readValue(jsonData, this.getClass());

			JsonNode arvore = objectMapper.readTree(jsonData);

			this.realmName = arvore.get(JSON_REALM_FIELD).asText();
			this.authServerUrl = arvore.get(JSON_URL_FIELD).asText();
			this.clientId = arvore.get(JSON_CLIENT_ID_FIELD).asText();
			this.clientSecret = arvore.path(JSON_CREDENTIALS_FIELD).get(JSON_CLIENT_SECRET_FIELD).asText();

		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static class Holder {
		static final KeycloakConfigure INSTANCE = new KeycloakConfigure();
	}

	public static KeycloakConfigure getInstance() {
		return Holder.INSTANCE;
	}

	public String getRealmName() {
		return realmName;
	}

	public String getAuthServerUrl() {
		return authServerUrl;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	@JsonProperty("realm")
	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	@JsonProperty("auth-server-url")
	public void setAuthServerUrl(String authServerUrl) {
		this.authServerUrl = authServerUrl;
	}

	@JsonProperty("resource")
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@JsonProperty("secret")
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
}
