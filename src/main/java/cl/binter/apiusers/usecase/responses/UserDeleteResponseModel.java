package cl.binter.apiusers.usecase.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class UserDeleteResponseModel extends UserResponse{
    private String message;

    public UserDeleteResponseModel(String name, String time) {
        super(time);
        this.message = "The user " + name + " has been deleted.";
    }


}

