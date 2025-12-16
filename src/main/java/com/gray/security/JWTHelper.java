package com.gray.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gray.Payloads.CustomUserDetails;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTHelper {

    // 30 minutes validity (as in your createToken)
	//30 * 60 * 1000L
    private static final long JWT_TOKEN_VALIDITY = 15 * 60 * 1000L;
    @Value("${jwt.secretKey}")
    private String SECRET;
 
    @Value("${jwt.refreshSecret}")
    private String REFRASH_SECRET;
    
    public double extractTokenversion(String token)
    {
    	return extractClaims(token,claims->claims.get("jwtTokenVersion",Double.class) );
    }
    
    public String extractRoles(String token) {
       return extractClaims(token,claims->claims.get("role",String.class));
    }

	public String extractUsername(String token) {
		
		return extractClaims(token,Claims::getSubject);
	}

	private <T>T extractClaims(String token, Function<Claims, T>resolver) {
		Claims claims=extractAllClaims(token);
		return resolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		Jwt<?, Claims> jwt=Jwts.parser()
				               .verifyWith(signingKey())
				               .build()
				               .parseSignedClaims(token);
		return jwt.getPayload();
	}

	private SecretKey signingKey() {
		byte [] key=Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(key);
	}

	public boolean validateToken(String token, CustomUserDetails userDetails) {
		
		final String userName=extractUsername(token);
		final double jwtTokenExpiretion=extractTokenversion(token);
		return (userName.equals(userDetails.getUsername()))
				&&!isTokenExpiration(token)
				&&jwtTokenExpiretion==(userDetails.getJwtTokenVersion());
				
	}

	private boolean isTokenExpiration(String token) {
		
		return isTokenExpirationWithDate(token).before(new Date());
	}

	private Date isTokenExpirationWithDate(String token) {
		return extractClaims(token, Claims::getExpiration);
	}

	public String generateToken(CustomUserDetails userDetails) {
		
		return generetToken(new HashMap<>(),userDetails);
	}

	private String generetToken(Map<String, Object> claims, CustomUserDetails userDetails) {
		//add user info
		String roles= userDetails.getAuthorities()
				    .stream()
				    .findFirst()
				    .map(role->role.getAuthority())
				    .orElse("No_Role");
		      claims.put("role", roles);
		  double jwtVersion=userDetails.getJwtTokenVersion();
		   claims.put("jwtTokenVersion", jwtVersion);
		   claims.put("userId",userDetails.getId());
		return Jwts.builder()
				   .claims(claims)
				   .subject(userDetails.getUsername())
				   .issuedAt(new Date(System.currentTimeMillis()))
				   .expiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY))
				   .signWith(signingKey(),Jwts.SIG.HS256)
				   .compact();
	}
	
//	public String refrashToken(CustomUserDetails userDetails)
//	{
//		return refrashToken( new HashMap<>(), userDetails);
//	}
//
//	private String refrashToken(Map<String, Object> claims,CustomUserDetails userDetails)
//	{
//		return Jwts.builder()
//				   .claims(claims)
//				   .subject(userDetails.getUsername())
//				   .issuedAt(new Date(System.currentTimeMillis()))
//				   .expiration(new Date(System.currentTimeMillis()+7*60*60*1000L))
//				   .signWith(refrashSigningKey(),Jwts.SIG.HS256)
//				   .compact();
//	}
//	
//	private SecretKey refrashSigningKey()
//	{
//		byte [] key=Decoders.BASE64.decode(REFRASH_SECRET);
//		return Keys.hmacShaKeyFor(key);
//	}
//    
    
//
//    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(
//            java.util.Base64.getDecoder().decode(SECRET)
//    );
//
//    // Generate token with email as subject
//    public String generateToken(String email) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, email);
//    }
//
//    // Optional: Generate token with custom claims
//    public String generateToken(Map<String, Object> extraClaims, String email) {
//        return createToken(extraClaims, email);
//    }
//
//    private String createToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .claims(claims)                    // Custom claims only
//                .subject(subject)                  // 'sub' claim
//                .issuedAt(new Date())              // 'iat' claim
//                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
//                .signWith(SIGNING_KEY, Jwts.SIG.HS256)
//                .compact();
//    }
//
//    // Extract username (subject)
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    // Extract expiration
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    // Generic claim extractor
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    // Parse and verify token
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(SIGNING_KEY)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    // Check if token is expired
//    private Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    // Validate token against UserDetails
//    public Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
	
	
}