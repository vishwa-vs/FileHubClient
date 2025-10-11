package com.filehub.client.usermanagement.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.filehub.client.audit.service.AuditService;
import com.filehub.client.usermanagement.entity.UserEntity;
import com.filehub.client.usermanagement.model.UserModel;
import com.filehub.client.usermanagement.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AuditService auditService;
	
	@PostMapping("/registerUser")
	public String registerUser(@RequestBody UserModel userModel) 
	{
		auditService.setValue(userModel.userName, "User management", "RegisterUser");
		return userService.registerUser(userModel);
	}
	
	@DeleteMapping("/deleteUser")
	public void deleteUser(@RequestBody Map<String, Integer> reqest)
	{
		userService.deleteUser(reqest.get("userId"));
	}
	
	@GetMapping("/viewUser")
	public UserModel viewUser(@RequestBody int userId)
	{
		return userService.viewUser(userId);
	}

	@GetMapping("/viewAllUser")
	public ArrayList<UserModel> viewAllUser(@RequestBody int adminUserId)
	{	
		return userService.viewAllUser(adminUserId);
	}
}
