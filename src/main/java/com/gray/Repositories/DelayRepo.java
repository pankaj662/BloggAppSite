package com.gray.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gray.Entity.DelayLogin;
import java.util.List;
import java.util.Optional;


@Repository
public interface DelayRepo extends JpaRepository<DelayLogin, Long> {

	Optional<DelayLogin> findByUserId(int userId);
}
