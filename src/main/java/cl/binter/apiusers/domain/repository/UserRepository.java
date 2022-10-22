package cl.binter.apiusers.domain.repository;

import java.util.List;

import cl.binter.apiusers.usecase.requests.UserDSRequestModel;

public interface UserRepository {
    public boolean existsByName(String name);
    public void save(UserDSRequestModel requestModel);
    public void delete(String name);
    public List<String> getAll();
}