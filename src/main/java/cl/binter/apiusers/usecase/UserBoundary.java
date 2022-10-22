package cl.binter.apiusers.usecase;

import cl.binter.apiusers.usecase.requests.UserDeleteRequestModel;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import cl.binter.apiusers.usecase.responses.UserResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
public interface UserBoundary {
    public UserResponse create(UserRequestModel requestModel);
    public UserResponse delete(UserDeleteRequestModel requestModel);
    public UserResponse getAll();
}
