package cl.binter.apiusers.domain.entities;

import java.time.LocalDateTime;

public class CommonUserFactory implements UserFactory{

    @Override
    public User create(String name, String password) {
        return new CommonUser(name, password);
    }

    @Override
    public User create(int id, String name, String password, String rol, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new CommonUser(id, name, password, rol, createdAt, updatedAt, deletedAt);
    }

}

