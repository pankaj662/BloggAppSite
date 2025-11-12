package com.gray.Exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourcesNotFoundExceptionwithString  extends RuntimeException
{

	private final String resourcesName;
	private final String filedName;
	private final String resourcesValue;
	
	public ResourcesNotFoundExceptionwithString(String resourcesName, String filedName, String resourcesValue) {
		super(String.format("%s with %s =%s Not found",resourcesName,filedName,resourcesValue));
		this.resourcesName = resourcesName;
		this.filedName = filedName;
		this.resourcesValue = resourcesValue;
	}	
}
