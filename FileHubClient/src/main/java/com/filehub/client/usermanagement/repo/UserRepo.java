package com.filehub.client.usermanagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.filehub.client.usermanagement.entity.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {

	UserEntity findByUserId(int adminUserId);

    UserEntity findByUserName(String username);

    UserEntity findByEmail(String email);
}
