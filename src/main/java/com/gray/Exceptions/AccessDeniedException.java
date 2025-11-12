package com.gray.Exceptions;

import com.gray.Enum.Role;

public class AccessDeniedException extends RuntimeException{
	
	private String message;
    private Role role;
	
	public AccessDeniedException(String message,Role role) {
		super(String.format("%s : %s",message,role));
		this.message = message;
		this.role=role;
	}
	
	

}
