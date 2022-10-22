package cl.binter.apiusers.usecase;

import cl.binter.apiusers.usecase.responses.UserResponse;

public interface UserPresenter {
    UserResponse prepareSuccessView(UserResponse user);
    UserResponse prepareConflictView(String error);
    UserResponse prepareNotFoundView(String error);
}
