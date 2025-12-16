package com.gray.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gray.Extra.AdvanceValidation;
import com.gray.Extra.BasicValidation;
import com.gray.Extra.NextLevleValidation;
import com.gray.Payloads.ApiResponse;
import com.gray.Payloads.CategoryDto;
import com.gray.Services.CategorieService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/veri-fied/blogg-app/maneger/categorie")
@Tag(name = "Categorie Manegment",description = "This apis use manege categoris")
@PreAuthorize("hasAnyRole('ADMIN','MANEGER')")
public class CategorieController {

	@Autowired
	private CategorieService categorieService;

	@PreAuthorize("hasAnyAuthority('ADMIN_CREATE','MANEGER_CREATE')")
	@PostMapping("/addCategorie")
	public ResponseEntity<CategoryDto> addCategorie(@Validated({ BasicValidation.class, AdvanceValidation.class,
			NextLevleValidation.class }) @RequestBody CategoryDto categoryDto) {
		String title = categoryDto.getCategorieTitle();
		if (this.categorieService.existTitle(title)) {
			categoryDto.setCategorieTitle("Title alredy exist");
			return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.ALREADY_REPORTED);
		}
		CategoryDto categoryDto2 = this.categorieService.addCategorie(categoryDto);
		return new ResponseEntity<CategoryDto>(categoryDto2, HttpStatus.CREATED);
	}
    
	@PreAuthorize("hasAnyAuthority('ADMIN_UPDATE','MANEGER_UPDATE')")
	@PutMapping("/updateCategorie/{categorieId}")
	public ResponseEntity<CategoryDto> updateCategorie(
			@Validated({ BasicValidation.class, AdvanceValidation.class,
					NextLevleValidation.class }) @RequestBody CategoryDto categoryDto,
			@PathVariable("categorieId") Integer id) {
		if (this.categorieService.existTitle(categoryDto.getCategorieTitle())) {
			categoryDto.setCategorieTitle("Title alredy exist");
			return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.ALREADY_REPORTED);
		}
		CategoryDto categoryDto2 = this.categorieService.updateCategorie(categoryDto, id);
		return new ResponseEntity<CategoryDto>(categoryDto2, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ADMIN_DELETE')")
	@DeleteMapping("/deleteCategorie/{categorieId}")
	public ResponseEntity<ApiResponse> deleteCategorie(@PathVariable("categorieId") Integer id) {
		this.categorieService.deleteCategorie(id);
		ApiResponse apiResponse = new ApiResponse("Categorie delete succesfully", true);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_READ')")
	@GetMapping("/getCategorieById/{categorieId}")
	public ResponseEntity<CategoryDto> getCategorieById(@PathVariable("categorieId") Integer id) {
		CategoryDto categoryDto = this.categorieService.getCategorieById(id);
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.ACCEPTED);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_READ')")
	@GetMapping("/getAllCategorie")
	public List<CategoryDto> getAllCategorie() {
		return this.categorieService.getAllCategorie();
	}
}
