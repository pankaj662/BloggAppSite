package com.gray.Utils;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class UserContextHolder {
	
	private static final ThreadLocal<String> curentUpdatingUserEmail=new ThreadLocal<>();

    private static final ThreadLocal<Integer> userId=new ThreadLocal<>();
    
    public static void setUserId(int id)
    {
    	userId.set(id);
    }
    
    public static Integer getUserId()
    {
    	System.out.println("in threadpool"+userId.get());
    	return userId.get();
    }
	public static void setCurentUpdatingUserEmail(String email)
	{
		curentUpdatingUserEmail.set(email);
		
	}
	
	public static String getCurentUpdatedEmail() {
		//System.out.println(curentUpdatingUserEmail.get()+"For ThreadPol");
		return curentUpdatingUserEmail.get();
		
	}

	public static void clearId()
	{
		userId.remove();
	}
	
	public static void clear()
	{
		curentUpdatingUserEmail.remove();
	}
}
