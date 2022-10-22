package cl.binter.apiusers.domain.entities;

public interface UserFactory {
    User create(String name, String password);
}

