package com.gray.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gray.Entity.RefreshTokenRotaion;
import java.util.List;



@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshTokenRotaion, Long> {

	Optional<RefreshTokenRotaion> findByToken(String token);
	
	List<RefreshTokenRotaion> findAllByUserId(long userId);
}
