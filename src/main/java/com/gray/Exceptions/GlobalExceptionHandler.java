package com.gray.Exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gray.Payloads.ApiResponse;
import com.gray.Payloads.FullApiResponce;

import io.jsonwebtoken.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourcesNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundException( ResourcesNotFoundException ex)
	{
		String message=ex.getMessage();
		ApiResponse apiResponse=new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EmptyException.class)
	public ResponseEntity<ApiResponse> emptyException(EmptyException ex)
	{
		ApiResponse apiResponse= new ApiResponse(ex.getMessage(),false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResourcesNotFoundExceptionwithString.class)
	public ResponseEntity<ApiResponse> resourcesNotFoundExceptionwithString(ResourcesNotFoundExceptionwithString ex)
	{
		String message=ex.getMessage();
		ApiResponse apiResponse=new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.NOT_FOUND);
	}
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<Map<String, String>> methodArgumentNotValideException(MethodArgumentNotValidException ex)
//	{
//		Map<String, String> result = new HashMap();
//		ex.getBindingResult().getAllErrors().forEach((error)->{
//			String field=((FieldError)error).getField();
//			String message=error.getDefaultMessage();
//			result.put(field, message);
//		});
//		return new ResponseEntity<Map<String,String>>(result,HttpStatus.BAD_REQUEST);
//	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> hendalMethodArgumentNotValideException(MethodArgumentNotValidException ex)
	{
		Map<String, String> result=new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error)->{
			String field=((FieldError)error).getField();
			String message=error.getDefaultMessage();
			result.put(field, message);
		});
		return new ResponseEntity<Map<String,String>>(result,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NewRuntimeException.class)
	public ResponseEntity<FullApiResponce> newRuntimeExceptionHandler(NewRuntimeException ex)
	{
		String massage=ex.getMassage();
		String email=ex.getUserName();
		String password=ex.getPassword();
		FullApiResponce fullApiResponce=new FullApiResponce(massage,email,password);
		return new ResponseEntity<FullApiResponce>(fullApiResponce,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ApiResponse>newresourceAlredyExistsException(ResourceAlreadyExistsException ex)
	{
		String massage=ex.getMessage();
		ApiResponse apiResponse=new ApiResponse(massage,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.ALREADY_REPORTED);
		
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse>accessDeniedException(AccessDeniedException e)
	{
		String message=e.getMessage();
		ApiResponse apiResponse=new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessDeniedUser.class)
	public ResponseEntity<ApiResponse> accessDeniedUser(AccessDeniedUser a)
	{
		String message=a.getMessage();
		ApiResponse apiResponse=new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ApiResponse> ioException(java.io.IOException e)
	{
		String getMessage=e.getMessage();
		String message="File oprations failed"+getMessage;
		ApiResponse apiResponse=new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse> illegalArgumentException(IllegalArgumentException e)
	{
		String message=e.getMessage();
		ApiResponse  apiResponse=new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
	}
}
