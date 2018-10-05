package br.unb.redes.cloudapps.zuul.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthModel implements UserDetails {

	private static final long serialVersionUID = -2494292485104387210L;

	private String cpfUsuario;
	private String jsonToken;
	private String keycloakToken;
	private String senha;
	private List<GrantedAuthority> grantedAuths;

	public AuthModel() {
		// alterar caso a aplicação precise de utilizar os recursos de roles do
		// Spring
		this.grantedAuths = new ArrayList<GrantedAuthority>();
		this.grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
	}

	public AuthModel(String cpf) {
		// alterar caso a aplicação precise de utilizar os recursos de roles do
		// Spring
		this.grantedAuths = new ArrayList<GrantedAuthority>();
		this.grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		this.cpfUsuario = cpf;
	}

	public String getCpfUsuario() {
		return cpfUsuario;
	}

	public void setCpfUsuario(String cpfUsuario) {
		this.cpfUsuario = cpfUsuario;
	}

	public String getJsonToken() {
		return jsonToken;
	}

	public void setJsonToken(String jsonToken) {
		this.jsonToken = jsonToken;
	}

	public String getKeycloakToken() {
		return keycloakToken;
	}

	public void setKeycloakToken(String keycloakToken) {
		this.keycloakToken = keycloakToken;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.grantedAuths;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.getSenha();
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String getUsername() {
		return this.cpfUsuario;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
