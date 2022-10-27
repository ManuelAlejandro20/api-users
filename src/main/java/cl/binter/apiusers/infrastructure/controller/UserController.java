package cl.binter.apiusers.infrastructure.controller;

import cl.binter.apiusers.usecase.JwtUtilService;
import cl.binter.apiusers.usecase.responses.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import cl.binter.apiusers.usecase.UserBoundary;
import cl.binter.apiusers.usecase.requests.UserRequestModel;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final UserBoundary userBoundary;
    UserDetailsService userDetailsService;
    private JwtUtilService jwtUtilService;
    private AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public UserResponse authenticate(@RequestBody UserRequestModel requestModel){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestModel.getName(),
                        requestModel.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(requestModel.getName());
        String jwt = jwtUtilService.generateToken(userDetails);
        return userBoundary.responseToken(jwt, requestModel.getName());

    }

    @PostMapping("/register")
    public UserResponse create(@RequestBody UserRequestModel requestModel){
        return userBoundary.create(requestModel);
    }

    @PostMapping("/users/delete")
    public UserResponse delete(@RequestBody UserRequestModel requestModel){
        return userBoundary.delete(requestModel);
    }

    @GetMapping("/users/all")
    public UserResponse getAll(){
        return userBoundary.getAll();
    }

    @GetMapping("/info")
    public UserResponse getInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userBoundary.getInfo(auth.getName());
    }

    @GetMapping("/users/available")
    public UserResponse getAllNotDeleted(){
        return userBoundary.getAllNotDeleted();
    }

    @GetMapping("/users/deleted")
    public UserResponse getAllDeleted(){
        return userBoundary.getAllDeleted();
    }

}

