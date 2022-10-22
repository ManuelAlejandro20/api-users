package cl.binter.apiusers.infrastructure.controller;

import org.springframework.web.bind.annotation.*;

import cl.binter.apiusers.usecase.UserBoundary;
import cl.binter.apiusers.usecase.requests.UserDeleteRequestModel;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import cl.binter.apiusers.usecase.responses.UserResponse;

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
    public UserResponse delete(@RequestBody UserDeleteRequestModel requestModel){
        return userBoundary.delete(requestModel);
    }

    @GetMapping("/users")
    public UserResponse getAll(){
        return userBoundary.getAll();
    }

}

