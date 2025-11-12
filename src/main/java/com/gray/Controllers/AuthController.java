package com.gray.Controllers;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.gray.Entity.User;
import com.gray.Exceptions.AccessDeniedUser;
import com.gray.Exceptions.ResourcesNotFoundExceptionwithString;
import com.gray.Extra.AdvanceValidation;
import com.gray.Extra.BasicValidation;
import com.gray.Extra.NextLevleValidation;
import com.gray.Payloads.CustomUserDetails;
import com.gray.Payloads.UserDto;
import com.gray.Repositories.UserRepositorie;
import com.gray.Services.BlockSystemService;
import com.gray.Services.UserService;
import com.gray.Services.Impl.CustomUserDetailsService;
import com.gray.Services.Impl.OTPServiceMailSender;
import com.gray.Utils.JwtRequset;
import com.gray.Utils.JwtResponse;
import com.gray.security.JWTHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authenticated Apis", description = "This apis using authenticated user")
public class AuthController {
	@Autowired
	private JWTHelper helper;

	@Autowired
	private CustomUserDetailsService userDetails;

	@Autowired
	private UserRepositorie userRepositorie;
//	@Autowired
//	private AuthenticationManager manager;
//	
	@Autowired
	private UserService userService;

	@Autowired
	private BlockSystemService blockSystemService;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private OTPServiceMailSender otpService;

	@Value("${project.image}")
	private String path;

	// @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@Valid @RequestBody JwtRequset jwtRequset) {
		// this.doAuthenticate(jwtRequset.getEmail(), jwtRequset.getPassword());
		CustomUserDetails userDetails = this.userDetails.loadUserByUsername(jwtRequset.getEmail());
		User user = this.userRepositorie.findByEmail(jwtRequset.getEmail())
				.orElseThrow(() -> new ResourcesNotFoundExceptionwithString("User", "Email", jwtRequset.getEmail()));

		if (!(encoder.matches(jwtRequset.getPassword(), user.getPassword()))) {
			throw new AccessDeniedUser("Plase provide a valide password");
		}

		if (blockSystemService.isUserBlocked(user)) {
			throw new AccessDeniedUser("User is :Blocked: contect to :Admin:");
		}

		if (!user.isActive()) {
			throw new AccessDeniedUser("User profile is deActive contect to:Admin:");
		}
		String token = this.helper.generateToken(userDetails);

		// Last Login

		userRepositorie.updateLastLogin(jwtRequset.getEmail(), LocalDateTime.now());
		userRepositorie.updateJwtTokenVersion(jwtRequset.getEmail(), 1.09);

		JwtResponse response = JwtResponse.builder().jwtToken(token).userName(userDetails.getUsername()).build();

		return new ResponseEntity<JwtResponse>(response, HttpStatus.OK);
	}

//	private void doAuthenticate(String email,String password)
//	{
//		UsernamePasswordAuthenticationToken authenticate=new UsernamePasswordAuthenticationToken(email, password);
//		try
//		{
//			this.manager.authenticate(authenticate);
//			
//		}
//		catch(Exception e)
//		{
//			throw new NewRuntimeException("Invalide Username or Password",email,password);
//		}
//		
//	}
//	
	// @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@PostMapping("/newUser/Register")
	public ResponseEntity<UserDto> newUserRegister(
			@Validated({ BasicValidation.class, AdvanceValidation.class,
					NextLevleValidation.class }) @RequestParam("image") MultipartFile file,
			@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("about") String about) throws Exception {
//		if (this.userService.emailExist(userDto.getEmail())) {
//			userDto.setName("Email Exist");
//			return new ResponseEntity<>(userDto, HttpStatus.ALREADY_REPORTED);
//		}
		UserDto userDto = new UserDto();
		userDto.setName(name);
		userDto.setEmail(email);
		userDto.setPassword(password);
		userDto.setAbout(about);

		UserDto userDto2 = this.userService.userRegister(userDto, file, path);
		return new ResponseEntity<UserDto>(userDto2, HttpStatus.OK);
	}

	//@Async
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
//	@PatchMapping("/changePassword/user/{id}")
//	public ResponseEntity<ApiResponse> changePassword(@PathVariable Integer id,@RequestParam(value = "password")String password)
//	{
//		String
//	}
	
	
}
