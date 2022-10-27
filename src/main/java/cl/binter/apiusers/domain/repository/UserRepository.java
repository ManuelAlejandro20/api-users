package cl.binter.apiusers.domain.repository;

import java.util.List;

import cl.binter.apiusers.domain.entities.User;
import cl.binter.apiusers.usecase.requests.UserRequestModel;

public interface UserRepository {
    boolean existsByName(String name);
    boolean isDeleted(String name);
    User getUserByName(String name);
    void save(UserRequestModel requestModel);
    void delete(UserRequestModel requestModel);
    List<User> getAll();
    List<User> getAll(boolean onlyDeleted);
}