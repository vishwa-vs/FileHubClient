package com.filehub.client.usermanagement.model;

import java.time.LocalDateTime;
import com.filehub.client.usermanagement.entity.UserEntity;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserModel {

    public UserModel(UserEntity userEntity) 
    {
		this.email = userEntity.getEmail();
        this.createdAt = userEntity.getCreatedAt();
        this.isBlckedUser = userEntity.getIsBlckedUser();
		this.lastLoggedIn = userEntity.getLastLoggedIn();
		this.password = userEntity.getPassword();
		this.phnNumber = userEntity.getPhnNumber();
		this.updatedAt = userEntity.getUpdatedAt();
		this.userId = userEntity.getUserId();
		this.userName = userEntity.getUserName();
		this.userRole = userEntity.getUserRole();
		this.userStatus = userEntity.getUserStatus();
    }

    public UserModel() {
	}

	public int userId;
	
	public String userName;
	
	public String password;
	
	public String userRole;

	public String userStatus;
	
	public Boolean isBlckedUser;
	
	public String lastLoggedIn;
	
	public String email;
	
	public Long phnNumber;

    public String createdAt;

    public String updatedAt;
}
