package com.gray.Exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewRuntimeException extends RuntimeException {
	private final String massage;
	private final String userName;
	private final String password;
	public NewRuntimeException(String massage, String userName, String password) {
		super(String.format("%s from %s,%s",massage,userName,password));
		this.massage = massage;
		this.userName = userName;
		this.password = password;
	}
}
