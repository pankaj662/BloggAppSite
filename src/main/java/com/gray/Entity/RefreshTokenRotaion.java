package com.gray.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRotaion {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY )
	@Column(unique = true)
	private long id;
	
	@Column(nullable = false)
	private long userId;
	
	@Column(nullable = false,length = 500,unique = true)
	private String token;
	
	@Column(nullable = false)
	private boolean valid;
	
	//@Columnnullable = false)
	private LocalDateTime expiry;
	
    private boolean revoked;

    private boolean used;

    private String userAgent;

    private LocalDateTime createdAt;
	
}
