package com.gray.Controllers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gray.Config.AdminMailConfig;
import com.gray.Entity.DelayLogin;
import com.gray.Entity.RefreshTokenRotaion;
import com.gray.Entity.User;
import com.gray.Exceptions.AccessDeniedUser;
import com.gray.Exceptions.EmptyException;
import com.gray.Exceptions.ResourcesNotFoundExceptionwithString;
import com.gray.Extra.AdvanceValidation;
import com.gray.Extra.BasicValidation;
import com.gray.Extra.NextLevleValidation;
import com.gray.Payloads.CustomUserDetails;
import com.gray.Payloads.UserDto;
import com.gray.Repositories.DelayRepo;
import com.gray.Repositories.RefreshTokenRepo;
import com.gray.Repositories.UserRepositorie;
import com.gray.Services.BlockSystemService;
import com.gray.Services.UserService;
import com.gray.Services.Impl.CustomUserDetailsService;
import com.gray.Services.Impl.OTPServiceMailSender;
import com.gray.Utils.JwtRequset;
import com.gray.Utils.JwtResponse;
import com.gray.Utils.UserContextHolder;
import com.gray.security.JWTHelper;
import com.gray.security.JWTRefrashTokenHelper;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/veri-fied/blogg-app/public/auth")
@Tag(name = "Authenticated Apis", description = "This apis using authenticated user")
public class AuthController {
	@Autowired
	private JWTHelper helper;

	@Autowired
	private JWTRefrashTokenHelper helper2;

	@Autowired
	private CustomUserDetailsService userDetails;

	@Autowired
	private UserRepositorie userRepositorie;
	
	@Autowired
	private UserService userService;

	@Autowired
	private BlockSystemService blockSystemService;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private OTPServiceMailSender otpService;

	@Autowired
	private UserContextHolder contextHolder;

	@Autowired
	private DelayRepo delayRepo;
	
	@Autowired
	private AdminMailConfig mailConfig;
	
	@Autowired
	private RefreshTokenRepo refreshTokenRepo;

	@Value("${project.image}")
	private String path;

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@Valid @RequestBody JwtRequset jwtRequset) {
		if (mailConfig.adminMail.stream().anyMatch(mail->jwtRequset.getEmail().equals(mail))) {
          
            
		}
		CustomUserDetails userDetails = this.userDetails.loadUserByUsername(jwtRequset.getEmail());
		User user = this.userRepositorie.findByEmail(jwtRequset.getEmail())
				.orElseThrow(() -> new ResourcesNotFoundExceptionwithString("User", "Email", jwtRequset.getEmail()));
		
		DelayLogin login = this.delayRepo.findByUserId(user.getId()).orElseGet(
				()->new DelayLogin(user.getId())
				);
		if (login.getDelay()==null||LocalDateTime.now().isAfter(login.getDelay())) {
			contextHolder.setUserId(user.getId());
			if (!(encoder.matches(jwtRequset.getPassword(), user.getPassword()))) {
				login.setUserId(user.getId());
				login.setAttempCount(login.getAttempCount() + 1);
				if (login.getAttempCount() >= 5) {
					login.setTimeManege(login.getTimeManege() + 1);
					login.setDelay(LocalDateTime.now().plus(Duration.ofSeconds(30 * login.getTimeManege())));
				}
				this.delayRepo.save(login);
				
				throw new AccessDeniedUser("Plasse provide a valide password");
			}
			// System.out.println("in login auth"+contextHolder.getUserId());

			if (blockSystemService.isUserBlocked(user)) {
				throw new AccessDeniedUser("User is :Blocked: contect to :Admin:");
			}

			if (!user.isActive()) {
				throw new AccessDeniedUser("User profile is deActive contect to:Admin:");
			}
			login.setAttempCount(0);
			login.setTimeManege(0);
			delayRepo.save(login);
			String token = this.helper.generateToken(userDetails);

			String refrashToken = this.helper2.refreshTokenGanrete(userDetails);
			// Last Login

			userRepositorie.updateLastLogin(jwtRequset.getEmail(), LocalDateTime.now());
			userRepositorie.updateJwtTokenVersion(jwtRequset.getEmail(), 1.09);

			JwtResponse response = JwtResponse.builder().jwtToken(token).jwtRefreshToke(refrashToken)
					.userName(userDetails.getUsername()).build();
			
			return new ResponseEntity<JwtResponse>(response, HttpStatus.OK);
		}
		{

			throw new EmptyException("You can try multipule attempts plase waite sum time");

		}
	}

	@PostMapping("/newUser/Register")
	public ResponseEntity<UserDto> newUserRegister(
			@Validated({ BasicValidation.class, AdvanceValidation.class,
					NextLevleValidation.class }) @RequestParam("image") MultipartFile file,
			@RequestParam String name, @RequestParam String email,
			@RequestParam String password, @RequestParam String about) throws Exception {
    	UserDto userDto = new UserDto();
		userDto.setName(name);
		userDto.setEmail(email);
		userDto.setPassword(password);
		userDto.setAbout(about);

		UserDto userDto2 = this.userService.userRegister(userDto, file, path);
		return new ResponseEntity<UserDto>(userDto2, HttpStatus.OK);
	}

	@PostMapping("/send-otp")
	public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		if (email == null || email.isBlank() || email.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
		}

		otpService.sendOTP(email);
		return ResponseEntity.ok(Map.of("message", "OTP sent successFully"));
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String otp = body.get("otp");

		if (email == null || otp == null)
			return ResponseEntity.badRequest().body(Map.of("error", "Email and OTP required"));

		boolean ok = otpService.verifyOTP(email, otp);
		if (ok) {
			return ResponseEntity.ok(Map.of("message", "OTP verified successfully"));
		} else {
			return ResponseEntity.status(400).body(Map.of("error", "Invalid or expired OTP"));
		}
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> value) {
		String refreshToken=value.get("refreshToken");
		System.out.println("RAW TOKEN = [" + refreshToken + "]");

		String userName = this.helper2.extractRefreshEmail(refreshToken);
		CustomUserDetails customUserDetails = this.userDetails.loadUserByUsername(userName);

		RefreshTokenRotaion refreshTokenRotaion=this.refreshTokenRepo.findByToken(refreshToken)
				                              .orElseThrow(()->new RuntimeException("Token invalide !"));
		if (LocalDateTime.now().isAfter(refreshTokenRotaion.getExpiry())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token ");

		}
		String jwtRefreshToken=null;
		String jwtAccessToken=null;
		try {
			
		 jwtRefreshToken = this.helper2.rotateRefreshToken(refreshToken, customUserDetails);
		 jwtAccessToken=this.helper.generateToken(customUserDetails);
		}
		catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(e.getMessage());
		}
		JwtResponse response = JwtResponse.builder().jwtToken(jwtAccessToken).jwtRefreshToke(jwtRefreshToken).userName(userName)
				.build();
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}
