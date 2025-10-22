package com.filehub.client.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz ->
                        authz

//                                .requestMatchers(HttpMethod.POST, "/user/registeruser").permitAll()
//                                .requestMatchers("/user/dashboard").authenticated()
//                                .anyRequest().permitAll()

                                // Public endpoints
                                .requestMatchers(HttpMethod.POST, "/user/*").permitAll()
                                .requestMatchers(HttpMethod.GET, "/user/registeruser").permitAll()
                                .requestMatchers("/user/*").permitAll()
                                .requestMatchers("/user/modifyuser","/user/block-toggle").hasRole("ADMIN")
                                .anyRequest().authenticated()

                                // Role-based URLs
//                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/user/dashboard").hasAnyRole("USER", "ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/user/loginuser")
                        .loginProcessingUrl("/user/postloginuser")
                        .defaultSuccessUrl("/user/successlogin",true)
                )
                .csrf(csrf -> csrf.disable());
        ;

        return http.build();

    }

    @Bean
    public UserDetailsService userDetailService() {
//		UserDetails user = User.withUsername("alice")
//				.password(passwordEncoder.encode("user123"))
//				.roles("USER")
//				.build();
//
//		UserDetails admin = User.withUsername("zack")
//				.password(passwordEncoder.encode("admin123"))
//				.roles("ADMIN")
//				.build();
//
//		return new InMemoryUserDetailsManager(user, admin);

        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authenticationManager() {
//        return new ProviderManager(List.of(authenticationProvider()));
//    }

}