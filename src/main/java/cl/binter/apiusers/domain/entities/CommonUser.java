package cl.binter.apiusers.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonUser implements User{

    private String name;
    private String password;

    @Override
    public boolean passwordIsValid() {
        return password != null && password.length() > 5;
    }

}

