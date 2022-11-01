package cl.binter.apiusers.usecase;

import cl.binter.apiusers.domain.entities.User;
import cl.binter.apiusers.domain.entities.UserFactory;
import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import cl.binter.apiusers.usecase.responses.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class UserInteractor implements UserBoundary{

    private final UserRepository userRepository;
    private final UserPresenter userPresenter;
    private final UserFactory userFactory;

    @Override
    public UserResponse create(UserRequestModel requestModel) {
        if (userRepository.existsByName(requestModel.getName())) {
            return userPresenter.prepareConflictView("User already exists.");
        }
        User user = userFactory.create(requestModel.getName(), requestModel.getPassword());
        if (!user.passwordIsValid()) {
            return userPresenter.prepareConflictView("User password must have more than 5 characters.");
        }

        userRepository.save(requestModel);

        UserResponseModel accountResponseModel = new UserResponseModel("User " + user.getName() + " has been created.");
        return userPresenter.prepareSuccessView(accountResponseModel);
    }

    @Override
    public UserResponse getAll() {
        List<User> users = userRepository.getAll();
        if(users.isEmpty()) {
            return userPresenter.prepareNotFoundView("No users registered.");
        }
        AllUserResponseModel responseGetAll = new AllUserResponseModel(users);
        return userPresenter.prepareSuccessView(responseGetAll);
    }

    @Override
    public UserResponse getAllNotDeleted() {
        List<User> users = userRepository.getAll(false);
        if(users.isEmpty()) {
            return userPresenter.prepareNotFoundView("No users available.");
        }
        AllUserResponseModel responseGetAll = new AllUserResponseModel(users);
        return userPresenter.prepareSuccessView(responseGetAll);
    }

    @Override
    public UserResponse getAllDeleted() {
        List<User> users = userRepository.getAll(true);
        if(users.isEmpty()) {
            return userPresenter.prepareNotFoundView("No users deleted.");
        }
        AllUserResponseModel responseGetAll = new AllUserResponseModel(users);
        return userPresenter.prepareSuccessView(responseGetAll);
    }

    @Override
    public UserResponse responseToken(String jwt, String username) {
        if(userRepository.isDeleted(username)){
            return userPresenter.prepareNotFoundView("This user has been deleted.");
        }
        UserResponseModel responseModel = new UserResponseModel(jwt);
        return userPresenter.prepareSuccessView(responseModel);
    }

    @Override
    public UserResponse getInfo(String username) {
        if(!userRepository.existsByName(username)) {
            return userPresenter.prepareNotFoundView("User does not exists.");
        }
        User user = userRepository.getUserByName(username);
        InfoResponseModel responseModel = new InfoResponseModel(user);
        return userPresenter.prepareSuccessView(responseModel);
    }

    @Override
    public UserResponse delete(UserRequestModel requestModel) {
        if (!userRepository.existsByName(requestModel.getName())) {
            return userPresenter.prepareNotFoundView("User does not exists.");
        }
        userRepository.delete(requestModel.getName());

        UserResponseModel responseDelete = new UserResponseModel("The user " + requestModel.getName() + " has been deleted.");
        return userPresenter.prepareSuccessView(responseDelete);
    }
}
