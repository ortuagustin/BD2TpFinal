package ar.edu.unlp.pasae.tp_integrador.config;

import static ar.edu.unlp.pasae.tp_integrador.config.JwtConfig.COOKIE_NAME;
import static ar.edu.unlp.pasae.tp_integrador.config.JwtConfig.EXPIRATION_TIME;
import static ar.edu.unlp.pasae.tp_integrador.config.JwtConfig.SECRET;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ar.edu.unlp.pasae.tp_integrador.entities.CustomUser;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,
			HttpServletResponse res) throws AuthenticationException {
		try {
			CustomUser creds = new ObjectMapper()
					.readValue(req.getInputStream(), CustomUser.class);

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getUsername(),
							creds.getPassword(),
							new ArrayList<>())
					);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req,
			HttpServletResponse response,
			FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		String token = JWT.create()
				.withSubject(((CustomUser) auth.getPrincipal()).getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(HMAC512(SECRET.getBytes()));


		Cookie cookie = new Cookie(COOKIE_NAME, token);
		cookie.setPath("/");
		response.addCookie(cookie);
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		response.getOutputStream().println("{\"success\": true}");
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_ACCEPTED); // Mando un status 200 para que jQuery no lo tome como erroneo
		response.getOutputStream().println("{\"success\": false}");
	}
}
