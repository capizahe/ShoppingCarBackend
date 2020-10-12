package com.backend.shoppingcar.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import org.springframework.stereotype.Repository;

import com.backend.shoppingcar.dto.Login;

@Repository
public class LoginRepository {

	private static final String LOGIN_DB_PATH = "logindb.txt"; 
	private BufferedReader bufferedReader;

	public LoginRepository() {
	}


	public Login getCredentials(String username) {

		try {
			getConnection();
			String line = null;
			StringTokenizer st;
			while((line = bufferedReader.readLine()) != null) {
				st = new StringTokenizer(line);

				String dbusername = st.nextToken();

				if(username.equals(dbusername)) {
					String dbpassword = st.nextToken();
					String dbrole = st.nextToken();
					boolean active = Boolean.getBoolean(st.nextToken());
					return new Login(dbusername, dbpassword, dbrole,active);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeConnection();
		}
		return null;
	}

	private BufferedReader getConnection() throws Exception {

		try {
			bufferedReader = new BufferedReader(new FileReader(LOGIN_DB_PATH));
			return bufferedReader;
		}catch(Exception e) {
			throw new Exception("No se puede acceder al archivo",e);
		}
	}

	private void closeConnection() {
		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
