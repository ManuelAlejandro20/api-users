package cl.binter.apiusers.usecase.requests;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDSRequestModel {
    private String name;
    private String password;
    private LocalDateTime creationTime;

}
