package com.filehub.client.usermanagement.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.filehub.client.usermanagement.model.UserModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int userId;
	
    @Column(nullable = false, name = "userName")
	public String userName;
	
    @Column(nullable = false, name = "password")
    @NotBlank
	public String password;
	
    @Column(nullable = false, name = "userRole")
	public String userRole;

    @Column(name = "userStatus")
	public String userStatus;
	
    @Column(nullable = false, name = "isBlckedUser")
	public Boolean isBlckedUser;
	
    @Column(name = "lastLoggedIn")
	public String lastLoggedIn;
	
    @Column(nullable = false, unique = true, name = "email")
	public String email;
	
    @Column(nullable = false, unique = true, name = "PhnNumber")
	public Long phnNumber;

    @CreatedDate
    @Column(updatable = false, name = "createdAt")
	public String createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt")
    public String updatedAt;
    
    public UserEntity(UserModel userModel) 
    {
		LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
		
		this.userId = userModel.userId;
		this.userName = userModel.userName;
		this.password = userModel.password;
		this.phnNumber = userModel.phnNumber;
		this.email = userModel.email;
		this.userRole = userModel.userRole;
		this.userStatus = userModel.userStatus;
		this.isBlckedUser = userModel.isBlckedUser;
		this.lastLoggedIn = userModel.lastLoggedIn;
		if(this.createdAt == null) 
		{
			this.createdAt = formattedDateTime;
		}
		this.updatedAt = formattedDateTime;

    }
}
