package spring;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityTest {
	@Test
	public void passwordEncoder() {
		String password = "1111";
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.print(password+"-->"+encoder.encode(password));
	}
}
