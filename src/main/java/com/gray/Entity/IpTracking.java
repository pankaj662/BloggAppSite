package com.gray.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
	    name = "ip",
	    uniqueConstraints  = {
	        @UniqueConstraint(columnNames = {"user_id", "ip_address"})
	    }
	  )  
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpTracking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true, nullable = false, updatable = false)
	private int userId;

	@Column(unique = true, nullable = false, updatable = false)
	private String ipAddress;
	
	private int request_reach_count;
	
    private LocalDateTime block_start_time;
	
	private int temp_block_count;

	private int total_temp_block;

	private boolean permanently_blocked;

	private LocalDateTime last_temp_blocked;

	public IpTracking(int userId, String ipAddress) {
		this.userId = userId;
		this.ipAddress = ipAddress;
	}

}
