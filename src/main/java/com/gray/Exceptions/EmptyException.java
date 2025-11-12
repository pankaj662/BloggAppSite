package com.gray.Exceptions;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmptyException extends RuntimeException implements Serializable
{
private static final long serializabelUID=1L;
private final String message;

public EmptyException(String message) {
	super(String.format("%s",message));
	this.message = message;	
}

}
