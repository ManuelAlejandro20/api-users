package cl.binter.apiusers.usecase;

import cl.binter.apiusers.usecase.requests.UserRequestModel;
import cl.binter.apiusers.usecase.responses.UserResponse;

/*
 *
 * Interface que contiene los métodos a implementar por la clase intereactor
 * que maneja la lógica de negocio de la aplicación
 *
 * */
public interface UserBoundary {
    UserResponse create(UserRequestModel requestModel);
    UserResponse update(UserRequestModel requestModel, String username);
    UserResponse delete(UserRequestModel requestModel);
    UserResponse getAll();
    UserResponse getAllNotDeleted();
    UserResponse getAllDeleted();
    UserResponse responseToken(String jwt, String username);
    UserResponse getInfo(String username);

}
