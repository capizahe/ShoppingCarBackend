package com.backend.shoppingcar.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAuthFilter extends OncePerRequestFilter {

	@Value("jwt.secret")
	private static String SECRET;

	@Value("apikey.header")
	private static String HEADER;



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		try {

			if(validateTokenHeader(request,response)) {
				Claims claims = validateTokenValue(request);

				if(claims.get("authorities") != null) {
					setSpringAuth(claims);
				} else {
					SecurityContextHolder.clearContext();
				}
			}else {
				SecurityContextHolder.clearContext();
			}
			
			filterChain.doFilter(request, response);

		}catch(Exception e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN,e.getMessage());
			return;
		}

	}



	private void setSpringAuth(Claims claims) {

		@SuppressWarnings("unchecked")
		List<String> authorities = (List<String>)claims.get("authorities");

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

		SecurityContextHolder.getContext().setAuthentication(auth);

	}



	private Claims validateTokenValue(HttpServletRequest request) {

		String jwtToken = request.getHeader(HEADER);
		return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
	}



	private boolean validateTokenHeader(HttpServletRequest request, HttpServletResponse response) {

		String authHeader = request.getHeader(HEADER);
		if(authHeader != null && !authHeader.isBlank()) {
			return true;
		}
		return false;
	}

}
