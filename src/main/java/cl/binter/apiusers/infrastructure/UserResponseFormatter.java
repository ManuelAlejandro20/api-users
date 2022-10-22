package cl.binter.apiusers.infrastructure;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import cl.binter.apiusers.usecase.UserPresenter;
import cl.binter.apiusers.usecase.responses.UserResponse;

public class UserResponseFormatter implements UserPresenter{

    @Override
    public UserResponse prepareSuccessView(UserResponse response) {
        LocalDateTime responseTime = LocalDateTime.parse(response.getTime());
        response.setTime(responseTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));
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