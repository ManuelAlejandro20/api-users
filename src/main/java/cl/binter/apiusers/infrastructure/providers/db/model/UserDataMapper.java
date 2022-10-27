package cl.binter.apiusers.infrastructure.providers.db.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserDataMapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "rol", nullable = false)
    private String rol;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public UserDataMapper(String name, String password){
        this.name = name;
        this.password = password;
    }

    @PrePersist
    public void prePersist(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))){
            this.rol = "ADMIN";
        }else{
            this.rol = "USER";
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
        this.deletedAt = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserDataMapper that = (UserDataMapper) o;
        return name != null && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
