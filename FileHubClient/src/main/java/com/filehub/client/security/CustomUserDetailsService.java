package com.filehub.client.security;

import java.util.Collections;

import com.filehub.client.usermanagement.entity.UserEntity;
import com.filehub.client.usermanagement.model.UserModel;
import com.filehub.client.usermanagement.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Fetch user from database
        System.out.println("Username: "+username);
        UserEntity user = userRepository.findByEmail(username);
        System.out.println(user.getUserName());
        UserModel userModel = new UserModel(user);

        return new User(userModel.getUserName(), userModel.getPassword(), Collections.singleton(new SimpleGrantedAuthority(userModel.getUserRole())));
    }


}