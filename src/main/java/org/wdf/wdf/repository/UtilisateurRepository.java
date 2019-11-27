package org.wdf.wdf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wdf.wdf.entity.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>{

	public Utilisateur findByLogin(String login);
	
}
