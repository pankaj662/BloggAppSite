package com.gray.Payloads;

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
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
	private int id;

	@NotBlank(message = "Title is required",groups = BasicValidation.class)
	@Size(min = 4, message = "Tile minimum 4 character",groups = AdvanceValidation.class)
	@Pattern(regexp = "[a-zA-Z0-9\\s\\-'.]+", message = "Title must contain only letters, numbers, spaces, hyphens, apostrophes, or periods",groups = NextLevleValidation.class)
	private String categorieTitle;

	@NotBlank(message = "Description is required",groups = BasicValidation.class)
	@Size(min = 30, message = "Minimum 30 charecter write",groups = AdvanceValidation.class)
	@Pattern(regexp = "[a-zA-Z0-9\\s\\-'.!?,;]+", message = "Description must contain only letters, numbers, spaces, and common punctuation marks",groups = NextLevleValidation.class)
	private String categorieDescription;

}
