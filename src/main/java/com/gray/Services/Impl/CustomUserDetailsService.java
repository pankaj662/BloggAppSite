package com.gray.Services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gray.Entity.User;
import com.gray.Exceptions.ResourcesNotFoundExceptionwithString;
import com.gray.Payloads.CustomUserDetails;
import com.gray.Repositories.UserRepositorie;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepositorie userRepositorie;

	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user=this.userRepositorie.findByEmail(username).orElseThrow(
    		   ()->new ResourcesNotFoundExceptionwithString("User", "Email", username)
    		   );
       
       return new CustomUserDetails(user.getEmail(), user.getPassword(), user.getRoles(), user.getJwtTokenVersion(),user.isActive());
	}

}
