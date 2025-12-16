//package com.gray.Services.Impl;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import org.springframework.stereotype.Service;
//
//import com.gray.Entity.RefreshTokenRotaion;
//import com.gray.Payloads.CustomUserDetails;
//
//@Service
//public class RefreceTokenService {
//
//	
//	//public RefreshTokenRotaion genreteRefreshToken(CustomUserDetails details)
//	{
//		
//		RefreshTokenRotaion rotaion=new RefreshTokenRotaion();
//		rotaion.setId(details.getId());
//		rotaion.setExpiry(LocalDateTime.now().plus(Duration.ofDays(15)));
//		rotaion.setCreatedAt(LocalDateTime.now());
//		rotaion.setRevoked(false);
//		rotaion.setToken(UUID.randomUUID().toString());
//		rotaion.setUsed(false);
////		rotaion.set
//	}
//}
