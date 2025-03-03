package br.com.avaliacao.AvaliacaoJava.configuration;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String token = request.getHeader("Authorization");
		if(token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
			
			if(jwtTokenUtil.validateToken(token)) {
				String username = jwtTokenUtil.getUsernameFromToken(token);
				
				
				List<SimpleGrantedAuthority> authorities = 
						List.of(new SimpleGrantedAuthority("USER"));
				
				Authentication authentication = new
						UsernamePasswordAuthenticationToken(username, null, authorities);
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			}
		}
		filterChain.doFilter(request, response);
	}

}