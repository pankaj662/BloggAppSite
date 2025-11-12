package com.gray.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

	ADMIN_READ("admin:read"),
	ADMIN_CREATE("admin:create"),
	ADMIN_UPDATE("admin:update"),
	ADMIN_DELETE("admin:delete"),
	
	MANEGER_READ("maneger:read"),
	MANEGER_CREATE("maneger:create"),
	MANEGER_UPDATE("maneger:update"),
	MANEGER_DELETE("maneger:delete"),
	
	USER_READ("user:read"),
	USER_CREATE("user:create"),
	USER_UPDATE("user:update"),
	USER_DELETE("user:delete"),
	
	VIEWER_READ("viewer:read");
	
	private final String permission;
	

}
