package com.gray.Enum;

import static com.gray.Enum.Permission.ADMIN_CREATE;
import static com.gray.Enum.Permission.ADMIN_DELETE;
import static com.gray.Enum.Permission.ADMIN_READ;
import static com.gray.Enum.Permission.ADMIN_UPDATE;
import static com.gray.Enum.Permission.MANEGER_CREATE;
import static com.gray.Enum.Permission.MANEGER_DELETE;
import static com.gray.Enum.Permission.MANEGER_READ;
import static com.gray.Enum.Permission.MANEGER_UPDATE;
import static com.gray.Enum.Permission.USER_CREATE;
import static com.gray.Enum.Permission.USER_DELETE;
import static com.gray.Enum.Permission.USER_READ;
import static com.gray.Enum.Permission.USER_UPDATE;
import static com.gray.Enum.Permission.VIEWER_READ;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Role {

	USER(Set.of(USER_UPDATE,USER_CREATE,USER_READ)),
	
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
