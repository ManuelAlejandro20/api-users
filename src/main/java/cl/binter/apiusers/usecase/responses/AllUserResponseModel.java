package cl.binter.apiusers.usecase.responses;

import cl.binter.apiusers.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllUserResponseModel extends UserResponse{
    private List<User> users;
}
