package com.gray.Services.Impl;

import java.security.SecureRandom;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class OTPServiceMailSender 
{ 
	private final StringRedisTemplate stringRedisTemplate;
	private final JavaMailSender javaMailSender;
	private final long expirySeconde;
	
	public OTPServiceMailSender(StringRedisTemplate stringRedisTemplate, JavaMailSender javaMailSender,
			@Value("${otp.expiry-seconde}") long expirySeconde) {
		super();
		this.stringRedisTemplate = stringRedisTemplate;
		this.javaMailSender = javaMailSender;
		this.expirySeconde=expirySeconde;
//		this.exprirySeconde = Long.parseLong(env.getProperty("otp.expiry-seconde"
//				+ "300"));
	}
	
	//======= GenretRendome OTP ======
	
	private String genrateOtp()
	{
		SecureRandom random=new SecureRandom();
		return String.valueOf(100000+random.nextInt(999999));
	}
	
	//=======   Send Otp to user emails  =======
	
	@Retryable(retryFor ={MailSendException.class},
			   noRetryFor = {RuntimeException.class},
			   maxAttempts = 3,
			   backoff = @Backoff(delay = 3000))
	public void sendOTP(String email)
	{
		String otp=genrateOtp();
		
		//===Stor otp ==
		String rateKey = "otp:rate:" + email;
		if (stringRedisTemplate.hasKey(rateKey)) {
		    throw new RuntimeException("Wait before requesting another OTP");
		}
		stringRedisTemplate.opsForValue().set(rateKey, "1", Duration.ofSeconds(60)); // 1 minute cooldown

		
		String key="otp:"+email;
		stringRedisTemplate.opsForValue().set(key,otp,Duration.ofSeconds(expirySeconde));
		
		//====== Build Mail=====
		
		SimpleMailMessage mailMessage=new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setSubject("Your OTP code");
		mailMessage.setText("Your OTP is:"+otp+"\n Is expire in 5 minutes");
		
        javaMailSender.send(mailMessage);
        
	}
	
	
	//====== verify and remove OTP=======
	public boolean verifyOTP(String email,String providedOtp)
	{
		String key="otp:"+email;
		String storedOtp=stringRedisTemplate.opsForValue().get(key);
		
		if(storedOtp==null)
		{
			return false;
		}
		
		boolean match=storedOtp.equals(providedOtp);
		if(match)
		{
			stringRedisTemplate.delete(key);
		}
		return match;
	}
	
	@Recover
	public void recover(MailSendException ex,String email,String otp)
	{
		System.out.println("Failedto send OTP to:"+email+"after retries.reason:"+ex.getMessage());
	}
//	public boolean changePassword(Integer id,String password)
//	{
//		User user=userRepositorie.findById(id).orElseThrow(
//				()->new ResourcesNotFoundException("user", "userd", id)
//				);
//		if(password.equals(user.getPassword())) {
//			throw new ResourceAlreadyExistsException("This password u alredy use plase choose difrent one:", password);
//		}
//		
//		sendOTP(user.getEmail());
//		
//		
//	}
	
	
}
