package cl.binter.apiusers.usecase;

import java.time.LocalDateTime;
import java.util.List;

import cl.binter.apiusers.domain.entities.User;
import cl.binter.apiusers.domain.entities.UserFactory;
import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.usecase.requests.UserDSRequestModel;
import cl.binter.apiusers.usecase.requests.UserDeleteRequestModel;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import cl.binter.apiusers.usecase.responses.UserDeleteResponseModel;
import cl.binter.apiusers.usecase.responses.UserResponse;
import cl.binter.apiusers.usecase.responses.UserResponseAllModel;
import cl.binter.apiusers.usecase.responses.UserResponseModel;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



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
        LocalDateTime now = LocalDateTime.now();
        UserDSRequestModel userDsModel = new UserDSRequestModel(user.getName(), user.getPassword(), now);

        userRepository.save(userDsModel);

        UserResponseModel accountResponseModel = new UserResponseModel(user.getName(), now.toString());
        return userPresenter.prepareSuccessView(accountResponseModel);
    }

    @Override
    public UserResponse getAll() {
        List<String> users = userRepository.getAll();
        if(users.isEmpty()) {
            return userPresenter.prepareNotFoundView("No users registered.");
        }
        LocalDateTime now = LocalDateTime.now();
        UserResponseAllModel responseGetAll = new UserResponseAllModel(users, now.toString());
        return userPresenter.prepareSuccessView(responseGetAll);
    }

    @Override
    public UserResponse delete(UserDeleteRequestModel requestModel) {
        if (!userRepository.existsByName(requestModel.getName())) {
            return userPresenter.prepareNotFoundView("User does not exists.");
        }
        LocalDateTime now = LocalDateTime.now();

        userRepository.delete(requestModel.getName());

        UserDeleteResponseModel responseDelete = new UserDeleteResponseModel(requestModel.getName(), now.toString());
        return userPresenter.prepareSuccessView(responseDelete);
    }
}
