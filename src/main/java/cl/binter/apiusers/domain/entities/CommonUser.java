package cl.binter.apiusers.domain.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonUser implements User{

    private int id;
    private String name;
    private String password;
    private String rol;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public CommonUser(String name, String password){
        this.name = name;
        this.password = password;
    }

    @Override
    public boolean passwordIsValid() {
        return password != null && password.length() > 5;
    }

}

