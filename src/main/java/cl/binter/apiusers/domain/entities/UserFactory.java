package cl.binter.apiusers.domain.entities;

public interface UserFactory {
    public User create(String name, String password);
}

