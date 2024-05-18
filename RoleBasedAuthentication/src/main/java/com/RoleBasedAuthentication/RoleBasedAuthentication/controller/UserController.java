package com.RoleBasedAuthentication.RoleBasedAuthentication.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RoleBasedAuthentication.RoleBasedAuthentication.entity.AuthRequest;
import com.RoleBasedAuthentication.RoleBasedAuthentication.entity.UserInfo;
import com.RoleBasedAuthentication.RoleBasedAuthentication.service.JwtService;
import com.RoleBasedAuthentication.RoleBasedAuthentication.service.UserInfoService;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome !!";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody UserInfo userInfo){
        return userInfoService.addUser(userInfo);

    }
    @PostMapping("/login")
    public String addUser(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if(authenticate.isAuthenticated()){
            return jwtService.generateToken(authRequest.getUserName());
        }else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }
    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('ADMIN_ROLES')")
    public List<UserInfo> getAllUsers(){
        return userInfoService.getAllUser();
    }
    @GetMapping("/getUsers/{id}")
    @PreAuthorize("hasAuthority('MANAGER_ROLES')")
    public UserInfo getAllUsers(@PathVariable Integer id){
        return userInfoService.getUser(id);
    }
}
