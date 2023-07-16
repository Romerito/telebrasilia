package br.com.telebrasilia;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.com.telebrasilia.upload.FilesStorageService;

/**
 * @author  Romerito Alencar
 */

@SpringBootApplication
@EnableWebMvc
public class TelebrasiliaApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(TelebrasiliaApplication.class, args);
	}	

	
 @Override
  public void run(String... arg) throws Exception {
    //storageService.deleteAll();
    storageService.init();
  }
}