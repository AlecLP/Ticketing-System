package com.synit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .apply(MyCustomDsl.customDsl())
                .flag(true).and()
                .authorizeRequests(requests -> requests
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/employeeDashboard").authenticated()
                        .requestMatchers("/viewTickets").hasAuthority("USER")
                        .requestMatchers("/ticketForm").hasAuthority("USER")
                		.requestMatchers("/managerDashboard").hasAuthority("MANAGER")
                		.requestMatchers("/adminDashboard").hasAuthority("ADMIN"))
                .exceptionHandling(handling -> handling.accessDeniedPage("/accessDeniedPage"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/employeeDashboard").permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());
		http.userDetailsService(userDetailsService);
		return http.build();
	}

}
