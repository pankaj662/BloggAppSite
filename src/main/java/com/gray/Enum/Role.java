package com.gray.Enum;

import static com.gray.Enum.Permission.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Role {

	USER(Set.of(USER_UPDATE)),
	
	ADMIN(Set.of(ADMIN_READ,ADMIN_CREATE,ADMIN_UPDATE,ADMIN_DELETE,
			     MANEGER_READ,MANEGER_CREATE,MANEGER_UPDATE,MANEGER_DELETE,
			     USER_READ,USER_CREATE,USER_UPDATE,USER_DELETE,VIEWER_READ)),
	
   MANEGER(Set.of(MANEGER_READ,MANEGER_CREATE,MANEGER_UPDATE,MANEGER_DELETE,
		          USER_READ,USER_CREATE,USER_UPDATE,USER_DELETE,VIEWER_READ)),
   
   VIEWER(Set.of(VIEWER_READ));
	

   private final Set<Permission> permissions;
   
   public List<SimpleGrantedAuthority> getAuthorities()
   {
	   var authorities=new ArrayList<>(getPermissions().stream().map(permission->new SimpleGrantedAuthority(permission.name())).toList());
	   authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
	   return authorities;
   }	
}
