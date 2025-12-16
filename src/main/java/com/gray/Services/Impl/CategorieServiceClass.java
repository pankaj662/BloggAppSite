package com.gray.Services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gray.Entity.Category;
import com.gray.Exceptions.EmptyException;
import com.gray.Exceptions.ResourcesNotFoundException;
import com.gray.Payloads.CategoryDto;
import com.gray.Repositories.CategorieRepository;
import com.gray.Services.CategorieService;

@Service
public class CategorieServiceClass implements CategorieService
{   
	@Autowired
	private CategorieRepository categorieRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public CategoryDto addCategorie(CategoryDto categoryDto) {
		Category category=this.categorieDtoToCategorie(categoryDto);
		Category savedCategory=this.categorieRepository.save(category);
		CategoryDto categoryDto2=this.categorieToCategorieDto(savedCategory);
		return categoryDto2;
	}

	@Override
	public CategoryDto updateCategorie(CategoryDto categoryDto, Integer id) {
		Category category = this.categorieRepository.findById(id).orElseThrow(()->
			new ResourcesNotFoundException("Categorie", "Id", id)
		);
		category.setCategorieTitle(categoryDto.getCategorieTitle());
		category.setCategorieDescription(categoryDto.getCategorieDescription());
		this.categorieRepository.save(category);
		return categoryDto;
	}

	@Override
	public void deleteCategorie(Integer id) {
		Category category=this.categorieRepository.findById(id).orElseThrow(()->
		            new ResourcesNotFoundException("Categorie", "Id", id));
		 this.categorieRepository.delete(category);
	}

	@Override
	public CategoryDto getCategorieById(Integer Id) {
		Category category=this.categorieRepository.findById(Id).orElseThrow(()->
		       new ResourcesNotFoundException("Categorie", "Id", Id));
		CategoryDto categoryDto=this.categorieToCategorieDto(category);
		return categoryDto;
	}

	@Override
	public List<CategoryDto> getAllCategorie() {
		 List<Category> categorieList=this.categorieRepository.findAll();
		 if(categorieList.isEmpty())
		 {
			 throw new EmptyException("Categorie is Emptey");
		 }
		 List<CategoryDto> categoryDto = categorieList.stream()
				          .map(category -> this.categorieToCategorieDto(category)).collect(Collectors.toList());
		 return categoryDto;
	}
	
	private Category categorieDtoToCategorie(CategoryDto categoryDto)
	{
		Category category=this.mapper.map(categoryDto, Category.class);
		return category;
	}
	
	private CategoryDto categorieToCategorieDto(Category category)
	{
		CategoryDto categoryDto=this.mapper.map(category, CategoryDto.class);
		return categoryDto;
	}
	
	public boolean existTitle(String title)
	{
		return this.categorieRepository.findByCategorieTitle(title).isPresent();
	}

}
