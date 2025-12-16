package com.gray.Services.Impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gray.Entity.User;
import com.gray.Enum.Role;
import com.gray.Exceptions.AccessDeniedException;
import com.gray.Exceptions.EmptyException;
import com.gray.Exceptions.ResourceAlreadyExistsException;
import com.gray.Exceptions.ResourcesNotFoundException;
//import com.gray.Payloads.ApiResponse;
import com.gray.Payloads.UserDto;
import com.gray.Repositories.UserRepositorie;
import com.gray.Services.UserService;
import com.gray.Utils.UserContextHolder;

@Service
public class UserServiceClass implements UserService {
	@Autowired
	private UserRepositorie userRepositorie;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserContextHolder userContextHolder;
	
	@Autowired
	private FileUplodeStstemClass fileUplodeStstem;
	
	private static final Logger log=LoggerFactory.getLogger(UserServiceClass.class);
	@Override
	@Transactional
	public UserDto userUpdate(UserDto userDto, Integer id) {
		
		Role currentRole=null;
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null&&auth.getAuthorities()!=null)
		{
		String roles=auth.getAuthorities()
				.stream().map(role->role.getAuthority())
				.filter(role->role.startsWith("ROLE_"))
				.findFirst()
				.orElse(null);
		
		if(roles!=null)
		{
		try
		{
			currentRole=Role.valueOf(roles.replace("ROLE_","").toUpperCase());
		}
		catch(IllegalArgumentException e)
		{
			log.error("Invalid role found in authentication:{}",roles);
	   }
     }
   }		
		
		if(currentRole ==null||!(currentRole==Role.ADMIN ||currentRole==Role.MANEGER||currentRole==Role.USER))
		{
			log.warn("User with role{} in not authorized to update user with user Id:{}",currentRole,id	);
			throw new AccessDeniedException("This user not applicabale to access this api ", currentRole);
		}
		
		
		
		log.info("Starting user update with Id:{}", id);
	    User user = this.userRepositorie.findById(id)
	            .orElseThrow(()->{
	            	log.error("User not found with Id:{}", id);
	            return	new ResourcesNotFoundException("User", "id", id);
	            });
	    log.debug("Setting current user email '{}' in UserContextHolder",user.getEmail());
            userContextHolder.setCurentUpdatingUserEmail(user.getEmail());
            //System.out.println(userContextHolder.getCurentUpdatedEmail()+"For User Update Api");
	    
            log.debug("Updating user fialde for Id:{}",id);
            
	    if (userDto.getName() != null) user.setName(userDto.getName());
	    log.debug("Updating Name {}->{}",user.getName(),userDto.getName());
	    if (userDto.getEmail() != null && !user.getEmail().equals(userDto.getEmail())) {
	    	log.debug("Chaching email availbality for:{}",userDto.getEmail());
	        if (this.emailExist(userDto.getEmail())) {
	        	log.warn("Email alredy exists :{}",userDto.getEmail());
	            throw new ResourceAlreadyExistsException("Email already in use: " , userDto.getEmail());
	        }
	        log.debug("Updating email:{}",user.getEmail());
	        user.setEmail(userDto.getEmail());
	    }
	    
	    if (userDto.getPassword() != null) {
	    	log.debug("Encoding and Updating passwored for Id:{}",user.getId());
	        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
	    }
	    if (userDto.getAbout() != null) user.setAbout(userDto.getAbout());
         log.debug("Updating user about for Id:{}",user.getId());
	   // user.setUpdatedAt(LocalDateTime.now());

	    User savedUser = userRepositorie.save(user);
	    log.info("User successFuly updated with Id:{}",user.getId());
	    return userToUserDto(savedUser);
	
	   
	}

	@Override
	public UserDto userGetById(Integer id) {
		log.info("GetUser with Id :{} ",id);
		User user = this.userRepositorie.findById(id)
				.orElseThrow(() -> {
                log.error("User not found by Id:{}",id);
				return new ResourcesNotFoundException("User", "Id", id);
					});
		
		log.info("User found with id:{}",user.getId());
		UserDto userDto = this.userToUserDto(user);
		log.info("User Return for Id:{}",id);
		return userDto;
	}

	@Override
	public List<UserDto> getAllUsers() {
		log.info("Get All user");
		List<User> users = this.userRepositorie.findAll();
		if (users.isEmpty()) {
			log.error("No any user");
			throw new EmptyException("Data base is empty no any user ");
		}
		log.debug("All User found and return");
		List<UserDto> collectDto = users.stream().map(user -> this.userToUserDto(user)).collect(Collectors.toList());
		return collectDto;
	}

	@Override
	public void userDelete(Integer id) {
		log.info("Found user to delete for Id:{}",id);
		User user = this.userRepositorie.findById(id)
				.orElseThrow(()->{
					log.error("User not found for Id:{}",id);
					return new ResourcesNotFoundException("User", "Id", id);
				});
      log.debug("User found and deleted for Id:{}",id);
		this.userRepositorie.delete(user);

	}

	@Override
	@Transactional
	public UserDto userRegister(UserDto userDto,MultipartFile file,String path)throws Exception {
		User user = this.userDtoToUser(userDto);
		
		if(userDto.getName()!=null)user.setName(userDto.getName());
		log.debug("Updating name{}->{}",user.getName(),userDto.getName());
		log.info("Check email alredy exist or not email:{}",userDto.getEmail());
		if(userDto.getEmail()!=null)
		{
			if(this.emailExist(userDto.getEmail()))
			{    log.warn("Email alredy register email:{}",user.getEmail());
				throw new ResourceAlreadyExistsException("Email alredy exists",userDto.getEmail());
			}
			log.debug("Set email to user:{}",userDto.getEmail());
			user.setEmail(userDto.getEmail());
		}
		log.info("Checking for password ");
		if(userDto.getPassword()!=null)
		{
			log.debug("Password checked and encoded");
		user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
		}
		user.setImage(this.fileUplodeStstem.fileUplodingForImage(path, file));
		
		log.info("Role set");
		user.setRoles(Role.USER);
		
		log.info("User created time and date save");
		user.setCreatedAt(LocalDateTime.now());
		
		log.info("User updated date and time save");
		user.setUpdatedAt(LocalDateTime.now());
		log.debug("User carete successFully");
		User saveUser = this.userRepositorie.save(user);
		
		UserDto userDto1 = this.userToUserDto(saveUser);
		userDto1.setPassword("password");
		return userDto1;

	}

	private User userDtoToUser(UserDto userDto) {
		log.debug("Mapping userDto to user Convert ");
		User user = this.modelMapper.map(userDto, User.class);
		return user;
	}

	private UserDto userToUserDto(User user) {
		log.debug("Mapping user to UserDtoConvert");
		UserDto userDto = this.modelMapper.map(user, UserDto.class);

//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setPassword(user.getPassword());
//		userDto.setAbout(user.getAbout());
		return userDto;
	}

	public boolean emailExist(String email) {
		log.debug("Email checking in databases email exist or Not");
		return this.userRepositorie.findByEmail(email).isPresent();
	}
	@Override
	public UserDto newUserRegister(UserDto userDto) {
		User user = this.userDtoToUser(userDto);
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		user.setRoles(Role.USER);
		//user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		//Role role = this.roleRepository.findById(AppConstants.NORMAL_USER).get();
        //user.getRoles().add(role);
		this.userRepositorie.save(user);
		UserDto newdto = this.userToUserDto(user);
		return newdto;
	}
}
