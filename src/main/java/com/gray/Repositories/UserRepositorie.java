package com.gray.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gray.Entity.User;
import com.gray.Enum.Role;

import java.time.LocalDateTime;
//import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositorie extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
	
	//Optional<User> findByUsername(String name);

@Modifying
@Transactional
@Query("UPDATE User u SET u.lastLogin=:time WHERE u.email=:email")
void updateLastLogin(@Param("email") String email,@Param("time") LocalDateTime time);

@Modifying
@Transactional
@Query("UPDATE User u SET u.jwtTokenVersion=:jwtTokenVersion WHERE u.email=:email")
void updateJwtTokenVersion(@Param("email") String email ,@Param("jwtTokenVersion") double jwtTokenVersion);

@Modifying
@Transactional
@Query("UPDATE User u SET u.active= :value WHERE u.roles= :role")
void allUserProfileUpdate(@Param("value") boolean value,@Param("role") Role role);
}
