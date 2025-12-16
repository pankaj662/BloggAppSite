package com.gray.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gray.Entity.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {

}
