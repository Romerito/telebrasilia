package br.com.telebrasilia;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author  Romerito Alencar
 */

@SpringBootTest
class TelebrasiliaApplicationTests {

	@Autowired
	TelebrasiliaApplication telebrasiliaApplication;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(telebrasiliaApplication);
	}

}
