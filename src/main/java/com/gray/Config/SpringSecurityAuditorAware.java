package com.gray.Config;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.gray.Enum.Role;
import com.gray.Utils.UserContextHolder;



@Component("auditorAware")
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Autowired
	private UserContextHolder userContextHolder;
		
	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		if(auth==null||!auth.isAuthenticated()||auth instanceof AnonymousAuthenticationToken)
		{
			return Optional.of("SELF(SELF)");
		}
		
		String curentUpdatedUserEmail=userContextHolder.getCurentUpdatedEmail();
		String userName=auth.getName();
//		User user=this.userRepositorie.findByEmail(userName).orElseThrow(
//				           ()->new ResourcesNotFoundExceptionwithString("User", "email", userName));
		//Integer id=user.getId();
		
		//System.out.println(curentUpdatedUserEmail);
		if(curentUpdatedUserEmail!=null&& curentUpdatedUserEmail.equals(userName))
		{
			//System.out.println(userContextHolder.getCurentUpdatedEmail()+"for SELF Before");
			userContextHolder.clear();
			//System.out.println(userContextHolder.getCurentUpdatedEmail()+"for SELf After");
			return Optional.of("SELF(SELF)");
		}
		Role role=auth.getAuthorities().stream()
				      .map(a-> a.getAuthority())
				      .filter(authName-> authName.startsWith("ROLE_"))
				      .map(roleName-> roleName.replace("ROLE_",""))
				      .map(roleName->{
				      try {
				    	  
				    	  return Role.valueOf(roleName);
				      }
				      catch(IllegalArgumentException e)
				      {
				    	  return Role.USER;
				      }
				      }  )
				      .findFirst()
				      .orElse(Role.USER);
		return Optional.of(userName+"(" +role+")");
	}

}
