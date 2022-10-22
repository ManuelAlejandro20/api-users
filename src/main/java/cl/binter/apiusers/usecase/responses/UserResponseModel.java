package cl.binter.apiusers.usecase.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseModel extends UserResponse{
    private String login;

    public UserResponseModel(String login, String time) {
        super(time);
        this.login = login;
    }

}
