package com.gray.security;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gray.Config.AppConstants;
import com.gray.Config.BotConfig;
import com.gray.Entity.IpTracking;
import com.gray.Payloads.CustomUserDetails;
import com.gray.Repositories.IpRepo;
import com.gray.Repositories.UserRepositorie;
import com.gray.Services.Impl.CustomUserDetailsService;
import com.gray.Utils.UserContextHolder;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	private UserRepositorie repositorie;
	@Autowired
	private JWTHelper jwtHelper;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private UserContextHolder contextHolder;
	
	@Autowired
	private BotConfig botConfig;
	
	@Autowired
	private IpRepo ipRepo;
	
	private  final Map<String, Bucket> ipBucket= new ConcurrentHashMap<>();
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	 	String  path=request.getRequestURI();
	 	if(path.startsWith("/api/v1/veri-fied/blogg-app/public/auth/")||path.startsWith("v3/api-docs")||path.startsWith("/swagger-ui/")
	 	 ||path.startsWith("api/v1/user/graphql/controller/apis"))
	 	{
	 	return true;	
	 	}
		return false;
	}
	
	private boolean recordBotHit(HttpServletRequest request) 
	{
		String ip=validIp(request);
		if(ipCheck(ip))
		{
         request.setAttribute("BLOCKED", true);
         return false;
		}
		String ua=request.getHeader("User-Agent");
		Integer userId=contextHolder.getUserId();
		if(userId==null)
		{
			userId=01;
		}
		Integer finalUserId=userId;
		IpTracking ipTracking=ipRepo.findByIpAddress(ip).orElseGet(
				()->
				new IpTracking(finalUserId,ip));
		ipTracking.setPermanently_blocked(true);
		ipRepo.save(ipTracking);
		return false;
	}
	//HonyBot detect
	private boolean isBotRequest(HttpServletRequest request)
	{
		String userAgent=request.getHeader("User-Agent");
		if(userAgent==null)
		{
			return false;
		}
		
		String ua=userAgent.toLowerCase();
		if(botConfig.BLOKED_USER_AGENTS.stream().anyMatch(bot-> ua.contains(bot)))
		{
			return true;
		}
		return false;
	}
	
	private String validIp(HttpServletRequest request)
	{
		String ip=request.getHeader("X-Forwarded-For");
		if(ip!=null&&ip.length()!=0&&!"unknown".equalsIgnoreCase(ip))
		{
			return ip.split(",")[0];
		}
		ip=request.getHeader("X-Real-Ip");
		if(ip!=null&&ip.length()!=0&&"unknown".equalsIgnoreCase(ip))
		{
			return ip;
		}
		return request.getRemoteAddr();
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String path=request.getRequestURI();
		if(botConfig.API_BLOCKED_ADMIN.stream().anyMatch(bot->path.equals(bot))
				|| botConfig.API_BLOCKED_USER.stream().anyMatch(bot->path.equals(bot))
				|| botConfig.EXTRAAPI.stream().anyMatch(bot->path.equals(bot))
					)
			{
				
				request.setAttribute("HONEYPOT", true);
				recordBotHit(request);
			}
		
		if(Boolean.TRUE.equals(request.getAttribute("BLOCKED")))
		{
			sendErrorResponse(response, 429, "Bot Activity detect this account is bolcked");
			return ;
		}
		
		if(Boolean.TRUE.equals(request.getAttribute("HONEYPOT")))
		{
			request.getRequestDispatcher("/honeypot/fake-news").forward(request, response);
			return;
		}
			
		String ipChack=validIp(request);
		IpTracking ip_Tracking=this.ipRepo.findByIpAddress(ipChack)
				.orElse(null);
		if(ip_Tracking!=null)
		{
		    if(ip_Tracking.isPermanently_blocked())
		       {
			     sendErrorResponse(response, 429,"Youer Ip is blocked");
			     return;
		       }
		}
		
            Integer userId=contextHolder.getUserId();
            if(userId==null)
            {
			String authHeader=request.getHeader("Authorization");
			String userName=null;
			String token=null;
			if(authHeader==null|| !authHeader.startsWith("Bearer "))
			{
			
				sendErrorResponse(response,HttpServletResponse.SC_UNAUTHORIZED, "You can not excess this apis");
				return;
			}
			if(authHeader!=null&&authHeader.startsWith("Bearer "))
			{
				token=authHeader.substring(7);
				userName=jwtHelper.extractUsername(token);
			}
            
			if(userName!=null)
			{
			CustomUserDetails customUserDetails=userDetailsService.loadUserByUsername(userName);
			userId=customUserDetails.getId();
			contextHolder.setUserId(userId);
			}
            }
            
		String ip=validIp(request);
		if(ipCheck(ip))
		{
			sendErrorResponse(response, 409, "User This ip already ragistred:"+ip);
			return;
		}
		final Integer finalUserId=userId;
		
		//check Bot
		if(isBotRequest(request))
		{
		IpTracking ipTracking=this.ipRepo.findByUserIdAndIpAddress(userId, ip)
				                  .orElseGet(()->new IpTracking(finalUserId,ip));
		ipTracking.setPermanently_blocked(true);
		ipRepo.save(ipTracking);
		sendErrorResponse(response, 429, "Bot activity detected. Your account is blocked.");
	        return; 
		}
		//Check User
		if(userId==null)
		{
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "User id missing");
			return;
		}
		IpTracking ipstats= this.ipRepo.findByUserIdAndIpAddress(userId, ip)
				.orElseGet(()-> new IpTracking(finalUserId,ip));
		
		//check permanent blocked
		if(ipstats.isPermanently_blocked())
		{
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Your account is permanently blocked");
			return;
     	}
		
		//check temporarily
		if(ipstats.getLast_temp_blocked()!=null&&ipstats.getLast_temp_blocked().isAfter(LocalDateTime.now()))
		{
			sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Your account is temporarily blocked");
			return;
     	}
		String bucketKey=userId+":"+ip;
		Bucket bucket=ipBucket.computeIfAbsent(bucketKey, k->newBucket());
		if(bucket.tryConsume(1))
		{
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
		else {
			if(ipstats.getRequest_reach_count()>=AppConstants.TEM_BLOCK_THRESHOLD)
			{
			//increment temp count
			ipstats.setRequest_reach_count(0);
			ipstats.setTemp_block_count(ipstats.getTemp_block_count()+1);
			
			if(ipstats.getTemp_block_count()>=AppConstants.TEM_BLOCK_THRESHOLD)
			{
				ipstats.setLast_temp_blocked(LocalDateTime.now().plus(AppConstants.BLOCK_TIME_DURATION));
				ipstats.setTemp_block_count(0);
				
				ipstats.setTotal_temp_block(ipstats.getTotal_temp_block()+1);
				if(ipstats.getTotal_temp_block()>=AppConstants.TEM_BLOCK_THRESHOLD)
				{
					repositorie.userProfileBlocked(userId, true);
					ipstats.setPermanently_blocked(true);
				}
				
				this.ipRepo.save(ipstats);
				
				if(ipstats.isPermanently_blocked())
				{
					
				sendErrorResponse(response,429,"Your account is permanently blocked");
				return;
				}
				else {
					sendErrorResponse(response,429, "Your Account is temporarily blockd");
					return;
				}
				
			}
			this.ipRepo.save(ipstats);
      	}
		else
		{
			if(AppConstants.VALUE==2)
			{
			ipstats.setIpAddress(ip);
			ipstats.setUserId(finalUserId);
			ipstats.setRequest_reach_count(ipstats.getRequest_reach_count()+1);
			ipstats.setBlock_start_time(LocalDateTime.now().plus(AppConstants.TEAM_BLOCK_DURATION));
			ipRepo.save(ipstats);
			
			AppConstants.VALUE=3;
			}
			if(LocalDateTime.now().isAfter(ipstats.getBlock_start_time()))
			{
				AppConstants.VALUE=2;
			}
			sendErrorResponse(response,429, "To many requests wait sum time");
			return;
		}
      }
	}
	
	private Bucket newBucket()
	{
	Bandwidth limit=Bandwidth.builder()
				                 .capacity(AppConstants.MAX_REQUEST_PER_MINUTE)
				                 .refillGreedy(AppConstants.MAX_REQUEST_PER_MINUTE, Duration.ofMinutes(1))
				                 .build();
		return Bucket.builder().addLimit(limit).build();
	}

	private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
		response.setStatus(status); 
		response.setContentType("application/json"); 
		response.setCharacterEncoding("UTF-8"); 
		response.getWriter().write("{\"error\":\"" + message + "\"}"); 
		response.getWriter().flush(); 
	}
	
	private boolean ipCheck(String ip)
	{
		return this.ipRepo.findByIpAddress(ip).isPresent();
	}
}

