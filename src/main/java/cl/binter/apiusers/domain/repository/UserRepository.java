package cl.binter.apiusers.domain.repository;

import java.util.List;

import cl.binter.apiusers.domain.entities.User;
import cl.binter.apiusers.usecase.requests.UserRequestModel;

/*
*
* Interface que contiene los métodos a implementar por la clase repositorio
*
* */
public interface UserRepository {
    boolean existsByName(String name);
    boolean isDeleted(String name);
    User getUserByName(String name);
    void save(UserRequestModel requestModel);
    void delete(String name);
    List<User> getAll();
    List<User> getAll(boolean onlyDeleted);
}