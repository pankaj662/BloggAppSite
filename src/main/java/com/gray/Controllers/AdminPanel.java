//package com.gray.Controllers;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.gray.Payloads.ApiResponse;
//import com.gray.Utils.AdminValue;
//
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("api/v1/admin")
//public class AdminPanel 
//{
//
//	@PreAuthorize("hashRole('ADMIN_CREATE')")
//	@PostMapping("/admin/login")
//	public ResponseEntity<ApiResponse> adminLogin(@Valid @RequestBody AdminValue adminValue)
//	{
//		
//	}
//	
//}
