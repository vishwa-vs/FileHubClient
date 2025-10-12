package com.filehub.client.usermanagement.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.filehub.client.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.filehub.client.usermanagement.entity.UserEntity;
import com.filehub.client.usermanagement.model.UserModel;
import com.filehub.client.usermanagement.repo.UserRepo;
import com.filehub.client.util.PasswordUtil;

@Service
public class UserService {
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	AuditService auditService;

	public String registerUser(UserModel userModel) 
	{
		auditService.setValue(userModel.userName, "User management", "RegisterUser");
		PasswordUtil passUtil = new PasswordUtil();
		UserEntity userEntity = new UserEntity(userModel);
		//password hashing 
		userEntity.password = passUtil.hashPassword(userEntity.password);
		// checkPassword(rawPassword, hashedPassword)
		userEntity.isBlckedUser = false;
		userRepo.save(userEntity);
		return "User registered";
	}

	public void loginUser(String userName)
	{
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = now.format(formatter);
		System.out.println("userName : "+userName);
		UserEntity userEntity = userRepo.findByUserName(userName);
		userEntity.setLastLoggedIn(formattedDateTime);
		UserModel userModel =  new UserModel(userEntity);
		auditService.setValue(userModel.userName, "Login", "Login");
	}
	
	public void deleteUser(int userId)
	{
		userRepo.deleteById(userId);
	}
	
	public UserModel viewUser(int userId)
	{
		UserEntity userEn =  userRepo.getReferenceById(userId);
		UserModel userModel = new UserModel(userEn);
		return userModel;
	}

	public ArrayList<UserModel> viewAllUser()
	{
		ArrayList<UserModel> userModelArr = new ArrayList<>();
		userModelArr = userModelList((ArrayList<UserEntity>) userRepo.findAll());
		
		return userModelArr;
	}
	
    public ArrayList<UserModel> userModelList(ArrayList<UserEntity> list) 
    {
    	ArrayList<UserModel> userModelArr = new ArrayList<>();
    	for(UserEntity entity : list ) 
    	{
    		UserModel userModel = new UserModel(entity);
    		userModelArr.add(userModel);
    	}
    	return userModelArr;
    }

}
