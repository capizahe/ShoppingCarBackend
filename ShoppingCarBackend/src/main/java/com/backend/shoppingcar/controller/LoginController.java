package com.backend.shoppingcar.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.backend.shoppingcar.dto.Login;
import com.backend.shoppingcar.dto.UserExt;
import com.backend.shoppingcar.repository.LoginRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Controller
public class LoginController {

	@Autowired
	private LoginRepository loginRepository;

	@Value("jwt.secret")
	private String secret;


	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam("email") String user, @RequestParam("password") String password){

		Login login = loginRepository.getCredentials(user);

		if(login != null) {
			if(login.getPassword().equals(password)) {
				//Usuario autenticado
				UserExt userExt = new UserExt(login.getUsername(), getToken(login.getUsername(), login.getRole()));
				//Pendiente generación token.
				return new ResponseEntity<>(userExt, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}	
	}


	private String getToken(String username, String rol) {

		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(rol);

		String token = Jwts.builder()
				.setSubject(username)
				.claim("authorities",grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 6000000))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
		
		return token;

	}

}
