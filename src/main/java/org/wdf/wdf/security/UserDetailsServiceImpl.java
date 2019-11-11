package org.wdf.wdf.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wdf.wdf.entity.AuthUser;
import org.wdf.wdf.entity.Utilisateur;
import org.wdf.wdf.repository.UtilisateurRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@SuppressWarnings("unused")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Utilisateur user=utilisateurRepository.findByLogin(username);
		AuthUser authUser= new AuthUser();
		Collection<GrantedAuthority> authorities=new ArrayList<>();
		if(!user.getLogin().isEmpty()){
			authUser.setUsername(user.getLogin());
			authUser.setPassword(user.getPassword()); 
			authorities.add(new SimpleGrantedAuthority(user.getProfil()));
		}
		if(authUser.getUsername().isEmpty())
			return null;			
		return new User(authUser.getUsername(),authUser.getPassword(),authorities);
	}
}
