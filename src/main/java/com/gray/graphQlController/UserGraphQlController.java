package com.gray.graphQlController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.gray.Payloads.UserDto;
import com.gray.Services.UserService;
@Controller
public class UserGraphQlController {

		@Autowired
		private UserService userService;

		@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_READ')")
		@QueryMapping
		public UserDto getUserById(@Argument("id") Integer id) {
			UserDto findUser = this.userService.userGetById(id);
			return  findUser;
		}

		@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_READ')")
		@QueryMapping
		public List<UserDto> getAllUser() {
			List<UserDto> allUser = this.userService.getAllUsers();
			System.out.println("GraphQL Users: " + allUser.size());
			return allUser;
		}
		
	}


