package com.gray.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gray.Entity.IpTracking;
import java.util.List;


public interface IpRepo extends JpaRepository<IpTracking, Long> {

	 Optional<IpTracking> findByUserIdAndIpAddress(int userId, String ipAddress);
	 
	 Optional<IpTracking> findByIpAddress(String ipAddress);
}
