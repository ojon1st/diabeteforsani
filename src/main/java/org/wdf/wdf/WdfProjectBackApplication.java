package org.wdf.wdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.wdf.wdf.entity.Utilisateur;
import org.wdf.wdf.repository.UtilisateurRepository;

@SpringBootApplication
public class WdfProjectBackApplication implements CommandLineRunner{

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	UtilisateurRepository utilisateurRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(WdfProjectBackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub	
		Utilisateur user=new Utilisateur();
		user.setNom("admin");
		user.setPrenom("admin");
		user.setLogin("admin");
		user.setPassword(bCryptPasswordEncoder.encode("admin"));
		user.setProfil("ADMINSUPPREME");
		utilisateurRepository.save(user);
	}

	@Bean
	public BCryptPasswordEncoder getBCRPE() {
		return new BCryptPasswordEncoder();
	}
}
