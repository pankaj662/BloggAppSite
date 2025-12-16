package com.gray.Controllers;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gray.Enum.Role;
import com.gray.Extra.AdvanceValidation;
import com.gray.Extra.BasicValidation;
import com.gray.Extra.NextLevleValidation;
import com.gray.Payloads.ApiResponse;
import com.gray.Payloads.UserDto;
import com.gray.Services.BlockSystemService;
import com.gray.Services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/veri-fied/blogg-app/user")
@Tag(name = "User Managment",description = "Apis for managing User")
//@Tag(name = "User Managment", description = "Apis for managing User")
//@SecurityRequirement(name = "bearerAuth")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private BlockSystemService blockSystemService;
	
	@Value("${project.image}")
	private String path;
	
	@PreAuthorize("hasAnyAuthority('ADMIN_CREATE','MANEGER_CRETAE')")
	@PostMapping("/Register")
	@Operation(summary = "Registre a new User",description = "Admin and User can Register")
	@ApiResponses(value = {
	
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",description = "User Create SuccessFuly",
					content = @Content(schema = @Schema(implementation = UserDto.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "Email alredy Exist",
			content = @Content(schema = @Schema(implementation =UserDto.class)))
			
	})
	public ResponseEntity<UserDto> Register(@Validated({ BasicValidation.class, AdvanceValidation.class,
			NextLevleValidation.class }) @RequestParam("image") MultipartFile file,
			                             @RequestParam("name") String name,
			                             @RequestParam("email")String email,
			                             @RequestParam("password") String password,
			                             @RequestParam("about") String about	
			)throws Exception {
//		if (this.userService.emailExist(userDto.getEmail())) {
//			userDto.setName("Email Exist");
//			return new ResponseEntity<>(userDto, HttpStatus.OK);
//		}
		UserDto userDto =new UserDto();
		userDto.setName(name);
		userDto.setEmail(email);
		userDto.setPassword(password);
		userDto.setAbout(about);
		UserDto createUserDto = this.userService.userRegister(userDto,file,path);
		return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_READ')")
	@GetMapping("/getUserById/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Integer id) {
		UserDto findUser = this.userService.userGetById(id);
		return new ResponseEntity<>(findUser, HttpStatus.FOUND);
	}

	
	@PreAuthorize("hasAnyAuthority('ADMIN_UPDATE','MANEGER_UPDATE','USER_UPDATE')")
	@PutMapping("/updateUser/{userId}")
	public ResponseEntity<UserDto> updateUser(
	        @Validated({ BasicValidation.class, AdvanceValidation.class, NextLevleValidation.class })
	        @RequestBody UserDto userDto,
	        @PathVariable("userId") Integer id) {

	    UserDto updatedUser = userService.userUpdate(userDto, id);
	    return ResponseEntity.ok(updatedUser);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN_DELETE','MANEGER_DELETE','USER_DELETE')")
	@DeleteMapping("/deleteUser/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer id) {
		this.userService.userDelete(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User delete succesFuly", true), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN_READ','MANEGER_READ')")
	@GetMapping("/getAllUser")
	public ResponseEntity<List<UserDto>> getAllUser() {
		List<UserDto> allUser = this.userService.getAllUsers();
		return ResponseEntity.ok(allUser);
	}
	
	@PreAuthorize("hasAuthority('ADMIN_UPDATE')")
	@PostMapping("/temporaryBlock/{id}/Duration")
	public ResponseEntity<ApiResponse> userTemporaryBlock(@PathVariable int id,@RequestParam(value = "duration") String duration)
	{
		 Duration  duration1=parseDuration(duration);
		 this.blockSystemService.blockedUserTemporary(id, duration1);
		 ApiResponse apiResponse=new ApiResponse("User ;Temporary Blocked;",true);
		 return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMIN_UPDATE')")
	@PostMapping("/UNBlock/{id}")
	public ResponseEntity<ApiResponse> userUNBlock(@PathVariable int id)
	{
		 this.blockSystemService.unblockUserManualy(id);
		 ApiResponse apiResponse=new ApiResponse("User :UN Blocked:",true);
		 return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMIN_UPDATE')")
	@PostMapping("/ParmanentBlock/{id}")
	public ResponseEntity<ApiResponse> userParmanentBlock(@PathVariable int id)
	{
		 this.blockSystemService.blockedUserParmanently(id);
		 ApiResponse apiResponse=new ApiResponse("User :Parmanent Blocked:",true);
		 return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN_UPDATE','MANEGER_UPDATE')")
	@PostMapping("/deActiveProfile/{id}")
	public ResponseEntity<ApiResponse> userProfileIsDeActive(@PathVariable int id)
	{
		this.blockSystemService.userProfileDeactive(id);
		ApiResponse apiResponse=new ApiResponse("User Profile :deActive:",true);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN_UPDATE','MANEGER_UPDATE')")
	@PostMapping("/activeProfile/{id}")
	public ResponseEntity<ApiResponse> userProfileIsActive(@PathVariable int id)
	{
		this.blockSystemService.activeUserProfile(id);
		ApiResponse apiResponse=new ApiResponse("User Profile :Active:",true);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMIN_UPDATE')")
	@PostMapping("/allUserDeActive/{role}")
	public ResponseEntity<ApiResponse> allUserProfileDeactive(@PathVariable Role role)
	{
		this.blockSystemService.allUserProfileDeActive(role);
		ApiResponse apiResponse=new ApiResponse("All User Profile is :DeActive:",true);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMIN_UPDATE')")
	@PostMapping("/allUserActive/{role}")
	public ResponseEntity<ApiResponse> allUserProfileActive(@PathVariable Role role)
	{
		this.blockSystemService.allUserProfileActive(role);
		ApiResponse apiResponse=new ApiResponse("All User Profile is :Active:",true);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK);
	}
	
   private Duration parseDuration(String durationSTR)
   {
	   durationSTR=durationSTR.trim().toLowerCase();
	   if(durationSTR.endsWith("d"))
	   {
		   Long days=Long.parseLong(durationSTR.replace("d",""));
		   return Duration.ofDays(days);
	   }
	   else if(durationSTR.endsWith("h"))
	   {
		   Long hour=Long.parseLong(durationSTR.replace("h",""));
		   return Duration.ofHours(hour);
	   }
	   else if(durationSTR.endsWith("m"))
	   {
		   Long minute=Long.parseLong(durationSTR.replace("m",""));
		   return Duration.ofMinutes(minute);
	   }
	   else
	   {
		   throw new IllegalArgumentException("Invalid duration format! Use 10m, 2h, or 3d");
	   }
   }

}
