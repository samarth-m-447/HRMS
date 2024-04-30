package com.scii.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;														 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;																					
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.scii.controller.auth.AuthenticationFailureHandlerImpl;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfiguration{

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public HttpFirewall getHttpFirewall() {
		StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
		strictHttpFirewall.setAllowUrlEncodedDoubleSlash(true);
		return strictHttpFirewall;
	}

	// configure SecurityFilterChain
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
		     .sessionManagement(session -> 
		                       session
		                       .invalidSessionUrl("/")
		                       .sessionFixation((sessionFixation) -> sessionFixation
		                               .newSession())
		                       //.maximumSessions(1)
		                       //.maxSessionsPreventsLogin(true)
		                       
		                       )
				.authorizeHttpRequests().requestMatchers("/","/WEB-INF/jsp/**").permitAll()
				.requestMatchers("/resource/**").permitAll()
				.requestMatchers("/externalImages/**").permitAll()
				.requestMatchers("/saveForgotPassword").permitAll()
				.requestMatchers("/passwordResetPage").permitAll()
				.requestMatchers("/sendForgotPasswordMail").permitAll()
				 .requestMatchers("/userPasswordChange**").permitAll() 
				.requestMatchers("/user/**").hasAnyAuthority("ROLE_USERADMIN","ROLE_USER")
				.requestMatchers("/admin/**").hasAuthority("ROLE_USERADMIN")
				.requestMatchers("/externalImages/**").hasAnyAuthority("ROLE_USERADMIN","ROLE_USER")
				.requestMatchers("/login**").permitAll() 						
				.anyRequest().authenticated().and()
				.formLogin(form -> 
				                  form
				                   .loginPage("/login")
				                   .loginProcessingUrl("/hrmsLoginAuth")
				                   .defaultSuccessUrl("/login-success",true)
				                   .failureUrl("/login-fail").permitAll()
						           .failureHandler(new AuthenticationFailureHandlerImpl()).permitAll()
						    )
				.logout(logout -> 
				              logout
				                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						       .logoutSuccessUrl("/")
						       .deleteCookies("JSESSIONID")
						       .clearAuthentication(true)
						       .permitAll()
						);
		return http.build();
	}
}
 