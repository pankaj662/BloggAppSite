package com.gray.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gray.Entity.Category;



@Repository
public interface CategorieRepository extends JpaRepository<Category, Integer>
{
 Optional<Category> findByCategorieTitle(String categorieTitle);
}
