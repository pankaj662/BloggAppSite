package com.gray.Exceptions;

public class IllegalArgumentException extends RuntimeException
{

	private String message;

	public IllegalArgumentException(String message) {
		super(String.format("%s",message));
		this.message = message;
	}
	
}
