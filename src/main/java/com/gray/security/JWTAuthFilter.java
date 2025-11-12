package com.gray.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gray.Entity.User;
import com.gray.Exceptions.EmptyException;
import com.gray.Payloads.CustomUserDetails;
import com.gray.Repositories.UserRepositorie;
import com.gray.Services.Impl.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JWTHelper jwtHelper;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private UserRepositorie userRepositorie;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("JWT Filter called for: " + request.getRequestURI());

		// get token
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = this.jwtHelper.extractUsername(token);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			CustomUserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			try {
				if (this.jwtHelper.validateToken(token, userDetails)) {
					if (!userDetails.isEnabled()) {
						sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "User is Blocked or Inactive");
						return;
					}
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authToken);

				} else {
					sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Login first!");
		            return;
				}
			} catch (Exception e) {
				sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Malformed or corrupt JWT token!");
		        return;
			}

		}
		filterChain.doFilter(request, response);
	}

	private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
		response.setStatus(status); // HTTP status code सेट करता है (401, 403, 400 आदि)
		response.setContentType("application/json"); // Response का type JSON है
		response.setCharacterEncoding("UTF-8"); // Encoding UTF-8
		response.getWriter().write("{\"error\":\"" + message + "\"}"); // JSON message भेजता है
		response.getWriter().flush(); // तुरंत send कर देता है
	}
}
//login date code
//if(SecurityContextHolder.getContext().getAuthentication()!=null&&
// SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
//{
//	String email=SecurityContextHolder.getContext().getAuthentication().getName();
//	User user=userRepositorie.findByEmail(email).orElse(null);
//	
//	if(user!=null)
//	{
//		user.setLastLogin(LocalDateTime.now());
//		userRepositorie.save(user);
//	}
//}
