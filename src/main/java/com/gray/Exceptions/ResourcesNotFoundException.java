package com.gray.Exceptions;

import java.io.Serializable;

import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResourcesNotFoundException extends RuntimeException implements Serializable
{
 private static final long serialVersionUId= 1L; 	
 private final String resourcesName;
 private final String fieldName;
 private final long fieldValue;
  
public ResourcesNotFoundException(String resourcesName, String fieldName, long fieldValue) {
	super(String.format("%s with %s=%d not found", resourcesName,fieldName,fieldValue));
	this.resourcesName = resourcesName;
	this.fieldName = fieldName;
	this.fieldValue = fieldValue;
}
}
