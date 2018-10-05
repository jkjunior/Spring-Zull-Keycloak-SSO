package br.unb.redes.cloudapps.zuul.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		AuthModel authModel = new AuthModel(cpf);		
		return authModel;
	}

}
