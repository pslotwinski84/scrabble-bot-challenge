package com.rad.scrab.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/dodaj").permitAll().and()
				.authorizeRequests().antMatchers("/scrab/dodaj").permitAll().and().authorizeRequests()
				.antMatchers("/dodaj").anonymous().and().authorizeRequests().antMatchers("/scrab/dodaj").anonymous()
				.and().authorizeRequests().anyRequest().hasAnyRole("ADMIN", "USER").and().authorizeRequests()
				.antMatchers("/login**").permitAll().and().formLogin().loginPage("/login")
				.loginProcessingUrl("/loginAction").permitAll().and().logout().logoutSuccessUrl("/login").permitAll()
				.and().csrf().disable();
	}
}
