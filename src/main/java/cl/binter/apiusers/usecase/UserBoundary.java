package cl.binter.apiusers.usecase;

import cl.binter.apiusers.usecase.requests.UserDeleteRequestModel;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import cl.binter.apiusers.usecase.responses.UserResponse;

public interface UserBoundary {
    UserResponse create(UserRequestModel requestModel);
    UserResponse delete(UserDeleteRequestModel requestModel);
    UserResponse getAll();
}
