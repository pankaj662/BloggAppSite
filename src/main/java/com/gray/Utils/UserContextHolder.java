package com.gray.Utils;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class UserContextHolder {
	
	private static final ThreadLocal<String> curentUpdatingUserEmail=new ThreadLocal<>();

   
	public static void setCurentUpdatingUserEmail(String email)
	{
		curentUpdatingUserEmail.set(email);
		
	}
	
	public static String getCurentUpdatedEmail() {
		//System.out.println(curentUpdatingUserEmail.get()+"For ThreadPol");
		return curentUpdatingUserEmail.get();
		
	}

	public static void clear()
	{
		curentUpdatingUserEmail.remove();
	}
}
