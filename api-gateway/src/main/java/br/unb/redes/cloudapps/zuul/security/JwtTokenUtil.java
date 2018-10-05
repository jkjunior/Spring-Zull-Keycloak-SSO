package br.unb.redes.cloudapps.zuul.security;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.unb.redes.cloudapps.zuul.util.Constantes;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * Classe gerada utilizando-se da biblioteca jjwt, do site
 * https://www.jsonwebtoken.io/
 * 
 * @author joao.esper
 *
 */
@Component
public class JwtTokenUtil {
	// Padrão Singleton
	private JwtTokenUtil() {
	}

	static class Holder {
		static final JwtTokenUtil INSTANCE = new JwtTokenUtil();
	}

	public static JwtTokenUtil getInstance() {
		return Holder.INSTANCE;
	}

	public String gerarJsonWebToken(String cpf) {
		UUID jit = UUID.randomUUID();
		
		String token = Jwts.builder().setSubject(cpf).setId(String.valueOf(jit)).setExpiration(new Date(1300819380))
				.signWith(SignatureAlgorithm.HS256, Constantes.JSON_WEB_TOKEN_SECRET).compact();

		return token;
	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(Constantes.JSON_WEB_TOKEN_SECRET).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			return false;
		}
	}

	public String getUsernameFromToken(String authToken) {
		return Jwts.parser().setSigningKey(Constantes.JSON_WEB_TOKEN_SECRET).parseClaimsJws(authToken).getBody()
				.getSubject();
	}

}
