package cl.binter.apiusers.domain.entities;

import org.springframework.stereotype.Component;

public class CommonUserFactory implements UserFactory{

    @Override
    public User create(String name, String password) {
        return new CommonUser(name, password);
    }

}

