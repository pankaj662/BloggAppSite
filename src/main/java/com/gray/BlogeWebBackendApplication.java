package com.gray;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

import io.github.cdimascio.dotenv.Dotenv;

@EnableAsync
@EnableCaching
@EnableRetry
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class BlogeWebBackendApplication //implements CommandLineRunner
{
//	@Autowired
//	private PasswordEncoder passwordEncoder;
	
//	@Autowired
//	private PasswordEncoder encoder;
	
	
	public static void main(String[] args) {
		
		Dotenv dotenv=Dotenv.configure()
				            .ignoreIfMissing()
				            .load();
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("REFRESH_SECRET", dotenv.get("REFRESH_SECRET"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		//System.setProperty("DB_PASSWORD", null);
		System.setProperty("GMAIL_NAME", dotenv.get("GMAIL_NAME"));
		System.setProperty("GMAIL_PASSWORD", dotenv.get("GMAIL_PASSWORD"));
		
		SpringApplication.run(BlogeWebBackendApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
//	@Override
//	public void run(String... args) throws Exception {
//		//System.out.println(this.encoder.encode("Pankaj@123"));
//		
//		try
//		{
//		Role role=new Role();
//		role.setId(AppConstants.NORMAL_USER);
//		role.setName("USER_ROLE");
//		
//		this.roleRepository.save(role);
//		
//		Role role1=new Role();
//		role1.setId(AppConstants.ADMIN_USER);
//		role1.setName("ADMIN_ROLE");
//		
//		this.roleRepository.save(role1);
//		}
//		catch(Exception e)
//		{
//			throw new NewRuntimeException("Role Not Added", "USER_ROLE", "ADMIN_ROLE");
//		}
//	}
	


//	@Override
//	public void run(String... args) throws Exception {
//		
//	System.out.println(this.passwordEncoder.encode("123456"));	
//	}

}
