package ar.edu.unlp.pasae.tp_integrador.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	/**
	 * Cierra la sesion
	 * @param request Request enviado
	 * @param response Respuesta a enviar
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	/**
	 * Para obtener el usuario actual
	 * @return El usuario logueado actualmente
	 */
	@RequestMapping(value = "/currentUser", method = RequestMethod.GET)
	public Object currentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		return auth.getPrincipal();
	}
}
