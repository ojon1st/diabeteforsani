package org.wdf.wdf.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(bCryptPasswordEncoder);
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().antMatchers("/").permitAll();	
		http.authorizeRequests().antMatchers("/assets/**").permitAll();	
		http.authorizeRequests().antMatchers("/**js**").permitAll();	
		http.authorizeRequests().antMatchers("/**css**").permitAll();	
		http.authorizeRequests().antMatchers("/**html**").permitAll();	
		http.authorizeRequests().antMatchers("/login/**").permitAll();	
		http.authorizeRequests().antMatchers("/api/formation/list/**").permitAll();
		http.authorizeRequests().antMatchers("/api/depistage/list/**").permitAll();
		http.authorizeRequests().antMatchers("/api/soin/list/**").permitAll();
		http.authorizeRequests().antMatchers("/api/gallery/list/**").permitAll();
		http.authorizeRequests().antMatchers("/api/sensibilisation/list/**").permitAll();
		http.authorizeRequests().antMatchers("/api/utilisateurs/list/**").permitAll();

		//persmission admin
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/formation/save/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/formation/delete/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/depistage/save/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/depistage/delete/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.PUT,"/api/utilisateurs/update-passe/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/utilisateurs/list/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/utilisateurs/save/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/sensibilisation/save/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/sensibilisation/delete/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/gallery/save/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/gallery/delete/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/soin/save/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/soin/delete/**").hasAnyAuthority("ADMINSUPPREME","ADMINSITE");
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JWTAuthenticationFilter(authenticationManager()));
		http.addFilterBefore(new JWTAutorisationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
