package com.gray.Exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
	private  final String fieldMassage;
	private final String fieldValue;
	
	public ResourceAlreadyExistsException(String fieldMassage, String fieldValue) {
		super(String.format("%s:%s",fieldMassage,fieldValue));
		this.fieldMassage = fieldMassage;
		this.fieldValue = fieldValue;
	}
	

}
