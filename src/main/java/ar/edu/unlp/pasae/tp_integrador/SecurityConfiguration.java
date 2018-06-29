package ar.edu.unlp.pasae.tp_integrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import ar.edu.unlp.pasae.tp_integrador.entities.RoleName;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	AppAuthenticationProvider authProvider;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.cors().and()
		.csrf().disable() // Necesario porque sino ignora los request que no son GET (ncesita el CRSF token)
		.authorizeRequests()
//		.antMatchers("/**").authenticated() 
		.antMatchers("/scientist/**").hasAuthority(RoleName.SCIENTIST.toString())
//		.antMatchers("/admin/**").hasAuthority(RoleName.ADMIN.toString())
		.antMatchers("/admin/**").permitAll()
		.antMatchers("/clinical-doctor/**").hasAuthority(RoleName.CLINICAL_DOCTOR.toString())
		.antMatchers("/epidemiologist/**").hasAuthority(RoleName.EPIDEMIOLOGIST.toString())
		.antMatchers("/css/**", "/resources/**").permitAll()
		//.antMatchers("/secured-admin/**").hasAnyAuthority(RoleName.ADMIN.toString(), RoleName.SCIENTIST.toString()) // Multiples roles
		// Dejo las siguientes dos linea como ejemplo de la funcion "access()"        
		//.antMatchers("/admin/**").access("hasRole('ADMIN')")
		//.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
		.and().formLogin()
		.and().exceptionHandling().accessDeniedPage("/Access_Denied");

	}
}