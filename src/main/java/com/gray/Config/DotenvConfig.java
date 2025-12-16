//package com.gray.Config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import io.github.cdimascio.dotenv.Dotenv;
//
//@Configuration
//public class DotenvConfig {
//
//	@Bean
//	public Dotenv dotenv ()
//	{
//		return Dotenv.load();
//	}
//	//private final Dotenv dotenv=Dotenv.load();
//	
//	public String getJwtSecret()
//	{
//		return dotenv().get("JWT_SECRET");
//	}
//	
//	public String getRefreshSecret()
//	{
//		return dotenv().get("REFRESH_SECRET");
//	}
//	
//	public String getDBPassword()
//	{
//		return dotenv().get("DB_PASSWORD");
//	}
//}
