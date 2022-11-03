package cl.binter.apiusers.usecase.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UserResponseModel extends UserResponse{

    private Object response;

}
