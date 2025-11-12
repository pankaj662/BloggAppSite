package com.gray.Payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.gray.Extra.AdvanceValidation;
import com.gray.Extra.BasicValidation;
import com.gray.Extra.NextLevleValidation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
	private int id;
	
	@NotBlank(message = "Title is required",groups = BasicValidation.class)
	@Size(min = 10, message = "Title must be content minimum 10 ",groups = AdvanceValidation.class)
	@Pattern(regexp = "^[a-zA-Z0-9\\s\\-,.?!']*$", message = "Title can only contain letters, numbers, spaces, hyphens, commas, periods, question marks, or apostrophes",groups = NextLevleValidation.class)
	private String title;

	@NotBlank(message = "Content is required",groups = BasicValidation.class)
	@Size(min = 300, message = "Content must be minimum 300 charecter ",groups = AdvanceValidation.class)
	@Pattern(regexp = "^[a-zA-Z0-9\\s\\-,.?!'’\":;()\\[\\]]*$", message = "Content can only contain letters, numbers, spaces, and common punctuation",groups = NextLevleValidation.class)
	private String content;
     
	
	private String imageName;

	
	private Date addedDate;

	
	private CategoryDto category;

	
	private UserDto user;
	
	
	private Set<CommentDto> comments=new HashSet<>();
}
