package com.gray.Exceptions;

public class AccessDeniedUser extends RuntimeException {
	
	private final String massage;

	public AccessDeniedUser(String massage) {
		super(String.format("%s", massage));
		this.massage = massage;
	}

	
}
