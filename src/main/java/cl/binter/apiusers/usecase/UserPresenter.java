package cl.binter.apiusers.usecase;

import cl.binter.apiusers.usecase.responses.UserResponse;

/*
*
* Interface con todos los métodos que puede implementar la clase
* que devuelva las respuestas.
*
* */
public interface UserPresenter {
    UserResponse prepareSuccessView(UserResponse response);
    UserResponse prepareConflictView(String error);
    UserResponse prepareNotFoundView(String error);
}
