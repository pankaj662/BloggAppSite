package com.gray.Config;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class AdminMailConfig {
	
	public Set<String> adminMail=Set.of(
			"admin@gmail.com",
			"pankaj@gmail.com"
			);
}
