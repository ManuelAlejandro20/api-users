package cl.binter.apiusers.usecase;

import cl.binter.apiusers.usecase.responses.UserResponse;

public interface UserPresenter {
    public UserResponse prepareSuccessView(UserResponse user);
    public UserResponse prepareConflictView(String error);
    public UserResponse prepareNotFoundView(String error);
}
