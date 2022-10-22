package cl.binter.apiusers.infrastructure.providers.db.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataMapper {
    @Id
    private String name;
    private String password;
    private LocalDateTime creationTime;
}
