package com.filehub.client.usermanagement.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.filehub.client.audit.service.AuditService;
import com.filehub.client.filemanagement.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.filehub.client.usermanagement.entity.UserEntity;
import com.filehub.client.usermanagement.model.UserModel;
import com.filehub.client.usermanagement.repo.UserRepo;
import com.filehub.client.util.PasswordUtil;

import static com.filehub.client.FMConstants.*;

@Service
public class UserService {
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	AuditService auditService;
    public String registerUser(UserModel userModel)
	{
        userModel.setIsBlckedUser(false);
        userModel.setUserRole("PARTIAL");
        userModel.setUserStatus("Active");
		auditService.setValue(userModel.userName, "User management", "RegisterUser");
		PasswordUtil passUtil = new PasswordUtil();
		UserEntity userEntity = new UserEntity(userModel);
		//password hashing 
		userEntity.password = passUtil.hashPassword(userEntity.password);
		// checkPassword(rawPassword, hashedPassword)
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

    public ResponseEntity<ApiResponse<String>> modifyUser(UserModel updatedUser, String updatedBy)
    {
        try
        {
            int id = updatedUser.getUserId();
            UserModel existingUser = new UserModel(userRepo.findByUserId(id));
            if (existingUser.equals(updatedUser))
                return ResponseEntity.status(404).body(new ApiResponse<>(404,NO_USER_FOUND,"Given user is not found"));

            if (!existingUser.getUserName().equals(updatedUser.getUserName()))
            {
                existingUser.setUserName(updatedUser.getUserName());
            }
            if (!existingUser.getPassword().equals(updatedUser.getPassword()))
            {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (!existingUser.getUserRole().equals(updatedUser.getUserRole()))
            {
                existingUser.setUserRole(updatedUser.getUserRole());
            }
            if (!existingUser.getUserStatus().equals(updatedUser.getUserStatus()))
            {
                existingUser.setUserStatus(updatedUser.getUserStatus());
            }

            if (!existingUser.getIsBlckedUser().equals(updatedUser.getIsBlckedUser()))
            {
                existingUser.setIsBlckedUser(updatedUser.getIsBlckedUser());
            }
            if (!existingUser.getEmail().equals(updatedUser.getEmail()))
            {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (!existingUser.getPhnNumber().equals(updatedUser.getPhnNumber()))
            {
                existingUser.setPhnNumber(updatedUser.getPhnNumber());
            }

            UserEntity userEntity = new UserEntity(existingUser);
            String action = "Modified user: " + userEntity.getUserName();
            auditService.setValue(updatedBy,"User management",action);
            userRepo.save(userEntity);
            return ResponseEntity.ok().body(new ApiResponse<>(200,SUCCESS,"User have been modified"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500,INTERNAL_SERVER_ERROR,e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<String>> setBlockStatus(int userId, String updatedBy)
    {
        UserEntity userEntity = userRepo.findByUserId(userId);
        if (userEntity == null)
            return ResponseEntity.status(404).body(new ApiResponse<>(404,NO_USER_FOUND,"Given user ID is not found"));
        UserModel userModel = new UserModel(userEntity);
        userModel.setIsBlckedUser(!userModel.getIsBlckedUser());

        String msg = "Unblocked";
        if (userModel.isBlckedUser)
            msg = "Blocked";
        String action = msg + " user: " + userModel.getUserName();
        auditService.setValue(updatedBy,"User management",action);
        userEntity = new UserEntity(userModel);
        userRepo.save(userEntity);

        return ResponseEntity.ok().body(new ApiResponse<>(200,SUCCESS,"User have been "+msg));
    }

}
