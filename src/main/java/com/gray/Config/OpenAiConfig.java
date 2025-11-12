package com.gray.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Blogg Web Apis", 
                                version = "1.0", 
                                description = "", 
                                termsOfService = "",
                                contact = @Contact(
                                		name = "",
                                        email = "", 
                                        url = ""),
                                license = @License(
                                		name = "Apache Licenses 2.0", 
                                		url = ""
                                		)
                                    ), servers = {
		                                         @Server(
		                                        		 description = "Local Devlopment server",
		                                        		 url = "http://localhost:8080") },
                                    security = {
				                                 @SecurityRequirement(
				                                		 name = "bearerAuth") 
				                                 }
                                       )
                          @SecurityScheme(
                        		     name = "bearerAuth",
                        		     description = "JWT Authentication using Bearer Scheme",
                        		     scheme = "bearer",
                        		     type = SecuritySchemeType.HTTP,
                        		     bearerFormat = "JWT",
                        		     in = SecuritySchemeIn.HEADER
                        		       )
public class OpenAiConfig {}
