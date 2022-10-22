package cl.binter.apiusers.usecase.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseAllModel extends UserResponse{
    private List<String> users;

    public UserResponseAllModel(List<String> users, String time) {
        super(time);
        this.users = users;
    }

}
