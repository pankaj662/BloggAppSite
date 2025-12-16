package com.gray.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ADMIN_CRAETERIYA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false,updatable = false,insertable = false,unique = true)
	private Long id;
	
	@Column(nullable = false,updatable = false,unique = true,insertable = false)
	private String email;
	
	@Column(nullable = false,updatable = false,insertable = false,unique = true)
	private String password;
	
	@Column(nullable = false,updatable = false,insertable=false,unique=true)
	private LocalDateTime adminCreated;
	
	@Column(updatable = false,unique = true)
	private String adminDeviceIp;
}
