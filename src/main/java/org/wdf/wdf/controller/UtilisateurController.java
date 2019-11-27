package org.wdf.wdf.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wdf.wdf.entity.Utilisateur;
import org.wdf.wdf.repository.UtilisateurRepository;

@RestController
@RequestMapping(value="/api/utilisateurs")
@CrossOrigin("*")
public class UtilisateurController {
	
	@Autowired
	UtilisateurRepository utilisateurRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@PostMapping(value="/save")
	public Utilisateur SaveUtilisateur(@RequestBody Utilisateur user) {
		List<Utilisateur> utilisateurs=utilisateurRepository.findAll();
		int compteur=0;
		for(Utilisateur u:utilisateurs) {
			if(u.getLogin().equals(user.getLogin())) {
				compteur++;
			}
		}
		if(compteur>0) {
			return null;
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return utilisateurRepository.save(user);
	}
	

	@PutMapping(value="/update")
	public Utilisateur UpdateUtilisateur(@RequestBody Utilisateur user) {
		List<Utilisateur> utilisateurs=utilisateurRepository.findAll();
		int compteur=0;
		for(Utilisateur u:utilisateurs) {
			if(u.getLogin().equals(user.getLogin())) {
				compteur++;
			}
		}
		if(compteur>0) {
			return null;
		}
		return utilisateurRepository.save(user);
	}
	
	@GetMapping(value = "/list")
	public List<Utilisateur> GetListeUtilisateur(){
		return utilisateurRepository.findAll();
	}
	
	
	
	@DeleteMapping(value = "/delete/{id}")
	public void DeleteUtilisateur(@PathVariable long id) {
		this.utilisateurRepository.deleteById(id);
	}
	
	@PutMapping(value = "update-passe")
	public Utilisateur UpdatePassword(@RequestBody Utilisateur utilisateur) {
		Utilisateur user =utilisateurRepository.findByLogin(utilisateur.getLogin());
		utilisateur.setActive(true);
		utilisateur.setNom(user.getNom());
		utilisateur.setPrenom(user.getPrenom());
		utilisateur.setProfil(user.getProfil());
		utilisateur.setId(user.getId());
		utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));
		return utilisateurRepository.save(utilisateur);
	}
}
