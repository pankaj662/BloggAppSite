package com.gray.security;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gray.Entity.RefreshTokenRotaion;
import com.gray.Payloads.CustomUserDetails;
import com.gray.Repositories.RefreshTokenRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTRefrashTokenHelper {

	@Autowired
	private RefreshTokenRepo refreshTokenRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Value("${jwt.refreshSecret}")
	private String REFRESH_SECRET;
	
	public String extractRefreshEmail(String jwtToken)
	{
		return extractClaims(jwtToken,Claims::getSubject);
	}

	private int extrectUserId(String jwtToken)
	{
		return extractClaims(jwtToken, claims->claims.get("userId",Integer.class));
	}
	
	private <T> T extractClaims(String jwtToken,Function<Claims, T> resolver) {
		Claims claims=extractAllClaims(jwtToken);
		return resolver.apply(claims);
	}

	private Claims extractAllClaims(String jwtToken) {
		Jwt<?, Claims> jwt=Jwts.parser()
				               .verifyWith(refreshSigningKey())
				               .build()
				               .parseSignedClaims(jwtToken);
		return jwt.getPayload();
	}

	private SecretKey refreshSigningKey() {
		byte [] key=Decoders.BASE64.decode(REFRESH_SECRET);
		return Keys.hmacShaKeyFor(key);
	}
	
	public boolean refrashTokenValidate(String jwtToken,CustomUserDetails userDetails)
	{
		final String email=extractRefreshEmail(jwtToken);
		return (email.equals(userDetails.getUsername()))&&!isTokenExpired(jwtToken); 
	}


	private boolean isTokenExpired(String jwtToken) {
		
		return isTokenExpiredWithDate(jwtToken).before(new Date());
	}
	
	private Date isTokenExpiredWithDate(String jwtToken) {
		
		return extractClaims(jwtToken, Claims::getExpiration);
	}
	
	public String refreshTokenGanrete(CustomUserDetails customUserDetails)
	{
		return refreshTokenGanrete(new HashMap<>(), customUserDetails);
	}
	
	private String refreshTokenGanrete(Map<String, Object> claims,CustomUserDetails customUserDetails)
	{
		 final double version=customUserDetails.getJwtTokenVersion();
		claims.put("jwtTokenVersion", version);
		claims.put("userId",customUserDetails.getId());
		String token= Jwts.builder()
				   .claims(claims)
				   .subject(customUserDetails.getUsername())
				   .issuedAt(new Date(System.currentTimeMillis()))
				   .expiration(new Date(System.currentTimeMillis()+7*24*60*60*1000))
				   .signWith(refreshSigningKey(),Jwts.SIG.HS256)
				   .compact();
		
		
		RefreshTokenRotaion rotation =
		RefreshTokenRotaion.builder().createdAt(LocalDateTime.now()).expiry(LocalDateTime.now().plusDays(7))
		                             .token(token).used(false).valid(true).userId(extrectUserId(token)).build();
		refreshTokenRepo.save(rotation);                           
		 return token;
	}
	
	public String rotateRefreshToken(String token,CustomUserDetails customUserDetails)
	{
		RefreshTokenRotaion refreshTokenRotaion=this.refreshTokenRepo.findByToken(token)
				           .orElseThrow(()-> new RuntimeException("Invalide refresh token"));
		
		if(!refreshTokenRotaion.isValid()||refreshTokenRotaion.isUsed()||LocalDateTime.now().isAfter(refreshTokenRotaion.getExpiry()))
         {
        	 throw new RuntimeException("Refresh token expired or invalide");
         }
         
         refreshTokenRotaion.setUsed(true);
         refreshTokenRotaion.setValid(false);
         refreshTokenRepo.save(refreshTokenRotaion);
         
         return refreshTokenGanrete(customUserDetails);
         
	}
	
	
}
