package cl.binter.apiusers.infrastructure;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cl.binter.apiusers.usecase.responses.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import cl.binter.apiusers.usecase.UserPresenter;

/*
*
* Clase que configura la respuestas a mandar por parte de la api
*
* */
public class UserResponseFormatter implements UserPresenter{

    @Override
    public UserResponse prepareSuccessView(UserResponse response) {
        LocalDateTime responseTime = LocalDateTime.now();
        response.setCurrentTime(responseTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));
        return response;
    }

    @Override
    public UserResponse prepareConflictView(String error) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, error);
    }

    @Override
    public UserResponse prepareNotFoundView(String error) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, error);
    }
}