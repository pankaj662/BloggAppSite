package com.gray.Payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto 
{

	@NotBlank(message = "Id is required")
	private int id;
	
	@NotBlank(message = "role is required")
	private String name;
}
