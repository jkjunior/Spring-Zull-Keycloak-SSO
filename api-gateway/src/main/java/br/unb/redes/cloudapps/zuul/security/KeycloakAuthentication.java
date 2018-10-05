package br.unb.redes.cloudapps.zuul.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.keycloak.OAuth2Constants;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.common.util.UriUtils;
import org.keycloak.constants.ServiceUrlConstants;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.util.JsonSerialization;

public class KeycloakAuthentication {

	static class TypedList extends ArrayList<RoleRepresentation> {
		private static final long serialVersionUID = 1L;
	}

	public static class Failure extends Exception {
		private static final long serialVersionUID = 1L;
		private int status;

		public Failure(int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}
	}

	public static AccessTokenResponse loginKeycloak(String username, String senha) {
		AccessTokenResponse token = new AccessTokenResponse();
		String json;

		try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

			HttpPost post = new HttpPost(KeycloakUriBuilder.fromUri(KeycloakConfigure.getInstance().getAuthServerUrl()).path(ServiceUrlConstants.TOKEN_PATH)
					.build(KeycloakConfigure.getInstance().getRealmName()));
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("username", username));
			formparams.add(new BasicNameValuePair("password", senha));
			formparams.add(new BasicNameValuePair(OAuth2Constants.GRANT_TYPE, "password"));
			formparams.add(new BasicNameValuePair(OAuth2Constants.CLIENT_ID, KeycloakConfigure.getInstance().getClientId()));
			formparams.add(new BasicNameValuePair(OAuth2Constants.CLIENT_SECRET, KeycloakConfigure.getInstance().getClientSecret()));
			UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
			post.setEntity(form);

			HttpResponse response = client.execute(post);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (status != 200 || entity == null) {
				token.setRefreshToken(null);
				return token;
			}
			json = getContent(entity);
			token = JsonSerialization.readValue(json, AccessTokenResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return token;
	}

	public static void logout(AccessTokenResponse res) throws IOException {

		try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
			HttpPost post = new HttpPost(KeycloakUriBuilder.fromUri(KeycloakConfigure.getInstance().getAuthServerUrl())
					.path(ServiceUrlConstants.TOKEN_SERVICE_LOGOUT_PATH).build(KeycloakConfigure.getInstance().getRealmName()));

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair(OAuth2Constants.REFRESH_TOKEN, res.getRefreshToken()));
			formparams.add(new BasicNameValuePair(OAuth2Constants.CLIENT_ID, KeycloakConfigure.getInstance().getClientId()));
			formparams.add(new BasicNameValuePair(OAuth2Constants.CLIENT_SECRET, KeycloakConfigure.getInstance().getClientSecret()));

			UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
			post.setEntity(form);

			HttpResponse response = client.execute(post);
			boolean status = response.getStatusLine().getStatusCode() != 204;

			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return;
			}

			InputStream is = entity.getContent();
			if (is != null)
				is.close();
			if (status) {
				throw new RuntimeException("failed to logout");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getContent(HttpEntity entity) throws IOException {
		if (entity == null)
			return null;
		InputStream is = entity.getContent();
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int c;
			while ((c = is.read()) != -1) {
				os.write(c);
			}
			byte[] bytes = os.toByteArray();
			String data = new String(bytes);
			return data;
		} finally {
			try {
				is.close();
			} catch (IOException ignored) {

			}
		}
	}

	public static String getRequestOrigin(HttpServletRequest request) {
		return UriUtils.getOrigin(request.getRequestURL().toString());
	}

	public static List<RoleRepresentation> getRealmRoles(AccessTokenResponse acessToken) throws Failure {
		InputStream is;
		List<RoleRepresentation> roles = null;

		try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
			HttpGet get = new HttpGet(KeycloakConfigure.getInstance().getAuthServerUrl() + "/admin/realms/" + KeycloakConfigure.getInstance().getRealmName()
					+ "/users/3f276b62-ba91-4a2d-8d92-cab84d99d810/role-mappings/realm");
			get.addHeader("Authorization", "Bearer " + acessToken.getToken());
			try {
				HttpResponse response = client.execute(get);

				if (response.getStatusLine().getStatusCode() != 200) {
					throw new Failure(response.getStatusLine().getStatusCode());
				}
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				try {
					roles = JsonSerialization.readValue(is, TypedList.class);
				} finally {
					is.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roles;
	}

}
