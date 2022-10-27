package cl.binter.apiusers.domain.entities;

public interface User {

    boolean passwordIsValid();
    String getName();
    String getPassword();
    String getRol();

}

