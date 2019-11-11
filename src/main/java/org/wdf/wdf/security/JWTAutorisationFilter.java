package org.wdf.wdf.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAutorisationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,Authorization");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization");
        response.setHeader("Access-Control-Max-Age", "3600");
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
        
	        String jwt=request.getHeader(SecuriteParams.HEADER_STRING);
			if (jwt == null || !jwt.startsWith(SecuriteParams.HEADER_PREFIX)) {
	            filterChain.doFilter(request, response);
	            return;
	        }
			try {
				Claims claims=Jwts.parser()
						.setSigningKey(SecuriteParams.SECRET)
						.parseClaimsJws(jwt.replace(SecuriteParams.HEADER_PREFIX, ""))
						.getBody();
				
				String username =claims.getSubject();
				@SuppressWarnings("unchecked")
				ArrayList<Map<String, String>> roles =  (ArrayList<Map<String, String>>) claims.get("roles");
				Collection<GrantedAuthority> authorities= new ArrayList<>();
				roles.forEach(role -> {
		            authorities.add(new SimpleGrantedAuthority(role.get("authority")));
		        });
				
				UsernamePasswordAuthenticationToken authenticatedUser =
		                 new UsernamePasswordAuthenticationToken(username, null, authorities);
		         SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
		         filterChain.doFilter(request, response);
			}catch(Exception e){
				
			}
        }
	}

}
