package cl.binter.apiusers.domain.entities;

import java.time.LocalDateTime;

public interface UserFactory {
    User create(String name, String password);
    User create(int id, String name, String password, String rol, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt);
}

