package com.gray.Payloads;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gray.Enum.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class CustomUserDetails implements UserDetails
{
	private String email;
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role roles;
	
	private double jwtTokenVerion;
	
	private boolean active;
	
	public CustomUserDetails(String email, String password,Role roles, double jwtTokenVerion,boolean active) {
		super();
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.jwtTokenVerion = jwtTokenVerion;
		this.active=active;
	}
	
	//public String 

	public double getJwtTokenVersion()
	{
		return this.jwtTokenVerion;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return roles.getAuthorities();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}
	
	@Override
	public boolean isEnabled() {
		return this.active;
	}

}
