package com.gray.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gray.Entity.Category;
import com.gray.Entity.Post;
import com.gray.Entity.User;

@Repository
public interface PostsRepositorie extends JpaRepository<Post, Integer> {

//	@Modifying
//	@Transactional
//	@Query("SELECT u FROM Post u WHERE u.id=:id")
	Page<Post>findByUser(Pageable pageable,User user);

	List<Post>findByCategory(Category category);
	
	//List<Post> findByTitleContaining(String title);
	List<Post> findByTitleContaining(String title);

}
