package com.gray.Config;
import static com.gray.Enum.Role.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gray.security.JWTAuthFilter;
import com.gray.security.JWTAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity 
@EnableMethodSecurity
public class SecurityConfig  {
	
	@Autowired
	private JWTAuthFilter filter;
	
	@Autowired
	private JWTAuthenticationEntryPoint point;
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		
		final String[] PUBLIC_URLS= {
				"/api/v1/auth/**",
				"/v3/api-docs",
				//"/api/v1/auth/newUser/Register",
				"/swagger-ui/**",
				"/v3/api-docs/swagger-config"
				
		};
//		http.authorizeHttpRequests(authz -> authz.requestMatchers("/public/**").permitAll()
//				.anyRequest().authenticated())
//		.httpBasic(Customizer.withDefaults())
//		.formLogin(form -> form.disable())
//		.csrf(csrf-> csrf.disable());
		
				http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(authz -> authz
			  .requestMatchers(PUBLIC_URLS).permitAll()
			  .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
			  .requestMatchers("/api/v1/maneger/**").hasAnyRole(MANEGER.name(),ADMIN.name())
			  .requestMatchers("/api/v1/user/**").hasAnyRole(USER.name(),MANEGER.name(),ADMIN.name())
			  .requestMatchers("/api/v1/veiwer/**").hasAnyRole(VIEWER.name(),MANEGER.name(),ADMIN.name(),USER.name())
		      .anyRequest().authenticated())
		.exceptionHandling(ex -> ex.authenticationEntryPoint(this.point))
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authenticationProvider);
		http.addFilterBefore(this.filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user=User.builder().username("user")
//				.password(passwordEncoder().encode("123456"))
//				.roles("USER").build(); 
//		UserDetails admin=User.builder().username("Admin")
//				.password(passwordEncoder().encode("654321"))
//				.roles("ADMIN").build();
//		return new InMemoryUserDetailsManager(user,admin);
//	}
//	
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception 
//	{
//		auth.userDetailsService(this.coustumUserDetails).passwordEncoder(passwordEncoder());
//	}
//	
//	@Bean
//	public PasswordEncoder passwordEncoder()
//	{
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean
//	public AuthenticationManager authemticationManager(AuthenticationConfiguration builder) throws Exception
//	{
//		return builder.getAuthenticationManager();
//	}
}
	