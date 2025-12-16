package com.gray.Payloads;

import com.gray.Extra.AdvanceValidation;
import com.gray.Extra.BasicValidation;
import com.gray.Extra.NextLevleValidation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private int id;
	@NotBlank(message = "Name is required", groups = BasicValidation.class)
	@Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name can conten only latters and space", groups = NextLevleValidation.class)
	@Size(min = 4, message = "Username must be min of 4 charecters !", groups = AdvanceValidation.class)
	private String name;

	@NotBlank(message = "Email is required", groups = BasicValidation.class)
	@Email(message = "Please provide a valid email address", groups = AdvanceValidation.class)
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Please provide a valid email address", groups = AdvanceValidation.class)
	private String email;

	@NotBlank(message = "Password is required", groups = BasicValidation.class)
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be include upercase,lowercase,number and special character", groups = NextLevleValidation.class)
	@Size(min = 8, message = "minimum length is 8", groups = AdvanceValidation.class)
	private String password;

	@NotBlank(message = "about is required", groups = BasicValidation.class)
	@Pattern(regexp = "^[a-zA-Z\\s]+$", message = " Can you write about, use only latters and space ", groups = NextLevleValidation.class)
	@Size(min = 30, message = "try to minimum 30 to 40 character", groups = AdvanceValidation.class)
	private String about;
	
	private String image;
	
	
	
	//private String role=SecurityContextHolder.getContext().getAuthentication().get;
//	@CreatedDate
//	private LocalDateTime createdAt;
//
//	@LastModifiedDate
//	private LocalDateTime updatedAt;
//
//	private LocalDateTime lastLogin;
	
	
	//private Set<RoleDto> roles = new HashSet<>();
}
