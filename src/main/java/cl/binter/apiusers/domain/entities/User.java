package cl.binter.apiusers.domain.entities;

public interface User {

    public boolean passwordIsValid();
    public String getName();
    public String getPassword();

}

