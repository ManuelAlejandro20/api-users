package cl.binter.apiusers.usecase.responses;

import cl.binter.apiusers.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InfoResponseModel extends UserResponse{

    private User user;

}
