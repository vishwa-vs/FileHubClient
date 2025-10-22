package com.filehub.client.usermanagement.controller;

import java.util.ArrayList;
import java.util.Map;

import com.filehub.client.filemanagement.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.filehub.client.audit.service.AuditService;
import com.filehub.client.usermanagement.entity.UserEntity;
import com.filehub.client.usermanagement.model.UserModel;
import com.filehub.client.usermanagement.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AuditService auditService;
	
	@PostMapping("/postregisteruser")
	public String registerUser(@RequestBody UserModel userModel) 
	{
		auditService.setValue(userModel.userName, "User management", "RegisterUser");
		return userService.registerUser(userModel);
	}

    @GetMapping("/registeruser")
    public String regUser()
    {
        System.out.println("registeruser");
        return "registeruser";
    }

	@GetMapping("/loginuser")
	public String loginUser()
	{
		System.out.println("loginuser");
		return "login";
	}

	@GetMapping("/successlogin")
	public String successLogin(Authentication authentication)
	{
		userService.loginUser(authentication.getName());
		System.out.println("successlogin");
		return "/dashboard";
	}

	@GetMapping("/dashboard")
	public String dashboard()
	{
		System.out.println("dashboard");
		return "dashboard";
	}
	
	@DeleteMapping("/deleteuser")
	public void deleteUser(@RequestBody Map<String, Integer> reqest)
	{
		userService.deleteUser(reqest.get("userId"));
	}
	
	@GetMapping("/viewuser")
	public UserModel viewUser(@RequestBody int userId)
	{
		return userService.viewUser(userId);
	}

	@GetMapping("/viewalluser")
	public ArrayList<UserModel> viewAllUser()
	{	
		return userService.viewAllUser();
	}

    @PostMapping("/modifyuser")
    public ResponseEntity<ApiResponse<String>> modifyUser(@RequestBody UserModel userModel, @RequestParam String updatedBy)
    {
        return userService.modifyUser(userModel,updatedBy);
    }
    @PostMapping("/block-toggle")
    public ResponseEntity<ApiResponse<String>> blockToggle(@RequestParam int id, @RequestParam String updatedBy)
    {
        return userService.setBlockStatus(id,updatedBy);
    }

}
