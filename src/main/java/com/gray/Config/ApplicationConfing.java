package com.gray.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class ApplicationConfing {

//	@Autowired
//	private UserRepositorie userRepositorie;
//	
//	@Autowired
//	private ModelMapper mapper;
//	@Bean
//	public UserDetailsService userDetailsService()
//	{
//		return userName->userRepositorie.findByEmail(userName).orElseThrow(
//				          ()->new ResourcesNotFoundExceptionwithString("UserName", "email", userName));
//	}
	
	@SuppressWarnings("deprecation")
	@Bean
	AuthenticationProvider authenticationProvider(UserDetailsService userDetails,PasswordEncoder passwordEncoder)
	{
		DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetails);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
		
	}
	@Bean
	 PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	@Bean
	 AuthenticationManager manager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		
		return authenticationConfiguration.getAuthenticationManager();
	}
		
//	@Bean
//	public UserInfo userInfo(String email)
//	{
//		return new UserInfo(email);
//	}
	
}
