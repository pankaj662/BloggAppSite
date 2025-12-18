package com.gray.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delay")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelayLogin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	private int userId;
	
	private int attempCount;
	
	private int timeManege;
	
	private LocalDateTime delay;
	
	public DelayLogin(int userId)
	{
		this.userId=userId;
	}
	
}
