package com.gray.Services;

import java.util.List;

import com.gray.Payloads.CategoryDto;

public interface CategorieService 
{
  CategoryDto addCategorie(CategoryDto categoryDto);
  
  CategoryDto updateCategorie(CategoryDto categoryDto, Integer id);
  
  void deleteCategorie(Integer id);
  
  CategoryDto getCategorieById(Integer Id);
  
  List<CategoryDto> getAllCategorie();
  
  boolean existTitle(String title);
}
