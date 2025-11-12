package com.gray.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

//import com.gray.Payloads.ApiResponse;
import com.gray.Payloads.UserDto;

public interface UserService {

	UserDto newUserRegister(UserDto userDto);

	UserDto userRegister(UserDto userDto,MultipartFile file,String path) throws Exception;

	UserDto userUpdate(UserDto userDto, Integer id);

	UserDto userGetById(Integer id);

	List<UserDto> getAllUsers();

	void userDelete(Integer id);

}
