/*~~(Index 0 out of bounds for length 0)~~>*/
package com.gray.Config;

import static com.gray.Enum.Role.ADMIN;
import static com.gray.Enum.Role.MANEGER;
import static com.gray.Enum.Role.USER;
import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

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
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gray.security.JWTAuthFilter;
import com.gray.security.JWTAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JWTAuthFilter filter;

	@Autowired
	private JWTAuthenticationEntryPoint point;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		final String[] PUBLIC_URLS = { "/api/v1/veri-fied/blogg-app/public/auth/**", "/v3/api-docs",
				// "/api/v1/auth/newUser/Register",
				"/swagger-ui/**", "/honeypot/**", "/v3/api-docs/swagger-config"

		};
//		http.authorizeHttpRequests(authz -> authz.requestMatchers("/public/**").permitAll()
//				.anyRequest().authenticated())
//		.httpBasic(Customizer.withDefaults())
//		.formLogin(form -> form.disable())
//		.csrf(csrf-> csrf.disable());

		http.csrf(csrf -> csrf.disable()).cors(withDefaults())
				.authorizeHttpRequests(authz -> authz.requestMatchers(PUBLIC_URLS).permitAll()
						.requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
						.requestMatchers("/api/v1/veri-fied/blogg-app/maneger/**")
						.hasAnyRole(MANEGER.name(), ADMIN.name()).requestMatchers("/api/v1/veri-fied/blogg-app/user/**")
						.hasAnyRole(USER.name(), MANEGER.name(), ADMIN.name())
						.requestMatchers("api/v1/user/graphql/controller/apis/**")
						.hasAnyRole(MANEGER.name(), ADMIN.name(), USER.name()).anyRequest().authenticated())

				.headers(header -> header
						// Content Security Policy → Stops XSS, script injection
						.contentSecurityPolicy(
								csp -> csp.policyDirectives("default-src 'self'; " + "script-src 'self'; "
										+ "style-src 'self' 'unsafe-inline'; " + "img-src 'self' data:; "
										+ "connect-src 'self'; " + "frame-ancestors 'none'; " + "object-src 'none';"))

						// Clickjacking Protection
						.frameOptions(frame -> frame.deny())

						// XSS Protection (For old browsers)
						// .xssProtection(xss->xss.block(true))

						// HSTS → Force HTTPS
						.httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000) // 1
																													// year
								.preload(true))

						// 5️⃣ Disable MIME Sniffing
						.contentTypeOptions(withDefaults())

						// 6️⃣ Protect privacy → No referrer leakage
						.referrerPolicy(ref -> ref.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))

						// 7️⃣ Disable dangerous browser features
//                .permissionsPolicy(p ->
//                        p.policy(
//                                "geolocation=(), " +
//                                "microphone=(), " +
//                                "camera=(), " +
//                                "fullscreen=(), " +
//                                "payment=()"
//                        )
//                )
						.addHeaderWriter(new StaticHeadersWriter("Permissions-Policy",
								"geolocation=(), microphone=(), camera=(), fullscreen=(), payment=()")))
				.exceptionHandling(ex -> ex.authenticationEntryPoint(this.point))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider);
		http.addFilterBefore(this.filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true);
		config.setAllowedOrigins(List.of("http://localhost:3000"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
		config.setAllowedHeaders(List.of("*"));
		config.setExposedHeaders(List.of("Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
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
