package cl.binter.apiusers.domain.repository;

import java.util.List;

import cl.binter.apiusers.usecase.requests.UserDSRequestModel;

public interface UserRepository {
    boolean existsByName(String name);
    void save(UserDSRequestModel requestModel);
    void delete(String name);
    List<String> getAll();
}