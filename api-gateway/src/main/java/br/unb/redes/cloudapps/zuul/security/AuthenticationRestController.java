package br.unb.redes.cloudapps.zuul.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationRestController {

	@Autowired
	private AuthController authController;

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> login(@RequestBody AuthRequestModel authenticationRequestModel) {

		final Authentication authentication = authController.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationRequestModel.getUsername(), authenticationRequestModel.getPassword()));

		if (authentication == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = JwtTokenUtil.getInstance().gerarJsonWebToken(authenticationRequestModel.getUsername());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", token);

		return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
	}
	
	@RequestMapping(value = "/teste/")
	public boolean teste(HttpServletRequest req, @RequestHeader HttpHeaders headers) {
		System.out.println("Metodo: "+req.getMethod());
		System.out.println("path: "+req.getPathInfo());
		System.out.println("Header content: "+headers.getContentType());
		return true;
		//return "Hello, você está logado. Seu CPF é: " + SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@RequestMapping("/alo")
	public String alo() {
		return "oi";
		//return "Hello, você está logado. Seu CPF é: " + SecurityContextHolder.getContext().getAuthentication().getName();
	}

	// Não utilizado, verificar com André
	@RequestMapping("/logout")
	public void logout() throws IOException {
		// AuthKeycloak.logout(accessToken);
	}

}
