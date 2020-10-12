package com.backend.shoppingcar.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.shoppingcar.dto.Login;
import com.backend.shoppingcar.repository.LoginRepository;

@Service
public class LoginService implements UserDetailsService{

	@Autowired
	LoginRepository loginRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Login login = loginRepository.getCredentials(username);

		if(login != null) {
			return new UserDetailsLogin(login);
		}else {
			throw new UsernameNotFoundException("No se encontró el usuario.");
		}



	}

}
