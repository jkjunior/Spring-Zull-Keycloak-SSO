package br.unb.redes.cloudapps.zuul.security;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Controle do processo de autenticação do usuário. Autentica o usuário no
 * Keycloak, caso ativo, ou no sistema Acesso Comprovantes, caso o usuário seja
 * Aposentado ou Pensionista
 * 
 * @author joao.esper
 */
@Service
public class AuthController implements AuthenticationProvider {

	/**
	 * Executa os procedimentos de autenticação, e retorna o objeto
	 * Authentication, para dizer ao Spring que o usuário está autenticado
	 */
	public Authentication authenticate(Authentication pAuth) throws AuthenticationException {
		String cpf = pAuth.getName();
		String senha = pAuth.getCredentials().toString();
		Authentication auth = null;
		boolean isValidLoginCheck = false;

		isValidLoginCheck = isValidLogin(cpf, senha);

		if (isValidLoginCheck) {
			UserDetails user = new AuthModel(cpf);
			auth = new UsernamePasswordAuthenticationToken(user, senha, user.getAuthorities());
			return auth;
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> pAuth) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(pAuth));
	}

	/**
	 * Verifica se as credenciais do usuário são válidas, autenticando no
	 * Keycloak, ou Acesso Comprovantes
	 * 
	 * @param cpf
	 * @param senha
	 * @return true, caso as credenciais sejam válidas, ou false, em caso contrário.
	 */
	public boolean isValidLogin(String username, String senha) {
		AuthModel authModel = new AuthModel();
		AuthDao authDao = new AuthDao();
		// se usuário for servidor ativo, então recupere o email, como username
		//username = authDao.servidorAtivo(cpf);
		if (username != null) {
			authModel = loginKeycloak(username, senha);
			if (authModel.getKeycloakToken() == null) {
				// não conseguiu logar no keycloak
				return false;
			}
		}
		//authDao.gravarDadosSessao(authModel);
		
		return true;
	}

	/**
	 * Autentica no Keycloak e retorna o modelo de autenticação com o atributo
	 * keyCloakToken preenchido
	 * 
	 * @param username
	 * @param senha
	 * @return true, em caso positivo, e false, em caso negativo
	 */
	private AuthModel loginKeycloak(String username, String senha) {
		AccessTokenResponse tokenKeycloak = new AccessTokenResponse();
		AuthModel authModel = new AuthModel();

		tokenKeycloak = KeycloakAuthentication.loginKeycloak(username, senha);

		if (tokenKeycloak.getRefreshToken() == null) {
			authModel.setKeycloakToken(null);
			return authModel;
		}
		authModel.setKeycloakToken(tokenKeycloak.getRefreshToken());
		return authModel;
	}

}
