package cl.binter.apiusers.infrastructure.controller;

import cl.binter.apiusers.usecase.responses.UserResponse;
import org.springframework.web.bind.annotation.*;

import cl.binter.apiusers.usecase.UserBoundary;
import cl.binter.apiusers.usecase.requests.UserRequestModel;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final UserBoundary userBoundary;

    @PostMapping("/register")
    public UserResponse create(@RequestBody UserRequestModel requestModel){
        return userBoundary.create(requestModel);
    }

    @PostMapping("/delete")
    public UserResponse delete(@RequestBody UserRequestModel requestModel){
        return userBoundary.delete(requestModel);
    }

    @GetMapping("/users/all")
    public UserResponse getAll(){
        return userBoundary.getAll();
    }

    @GetMapping("/users/notdeleted")
    public UserResponse getAllNotDeleted(){
        return userBoundary.getAllNotDeleted();
    }

    @GetMapping("/users/deleted")
    public UserResponse getAllDeleted(){
        return userBoundary.getAllDeleted();
    }

}

