package com.filehub.client.usermanagement.service;

import java.util.ArrayList;
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

	public String registerUser(UserModel userModel) 
	{
		PasswordUtil passUtil = new PasswordUtil();
		UserEntity userEntity = new UserEntity(userModel);
		//password hashing 
		userEntity.password = passUtil.hashPassword(userEntity.password);
		// checkPassword(rawPassword, hashedPassword)
		userEntity.isBlckedUser = false;
		userRepo.save(userEntity);
		return "User registered";
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

	public ArrayList<UserModel> viewAllUser(int adminUserId)
	{
		ArrayList<UserModel> userModelArr = new ArrayList<>();
		UserEntity userEntity =  userRepo.findByUserId(adminUserId);
		String userRole = userEntity.getUserRole();
		if(userRole.equalsIgnoreCase("admin")) 
		{
			userModelArr = userModelList((ArrayList<UserEntity>) userRepo.findAll());
		}
		
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
