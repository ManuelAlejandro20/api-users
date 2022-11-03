package cl.binter.apiusers.infrastructure.providers.db;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cl.binter.apiusers.domain.entities.User;
import cl.binter.apiusers.domain.entities.UserFactory;
import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.infrastructure.providers.db.model.UserDataMapper;

import cl.binter.apiusers.usecase.requests.UserRequestModel;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

/*
*
* Clase que se comunica con el repositorio de JPA para realizar operaciones
* CRUD
*
* */
@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository{

    private final DbUserRepository repository;
    private final UserFactory userFactory;

    /*
    *
    * Método que devuelve un booleano que nos indica si el usuario existe o no
    *
    * */
    @Override
    public boolean existsByName(String name) {
        return repository.getUserByName(name) != null;
    }

    /*
    *
    * Devuelve un un booleano dependiendo si el usuario ha sido eliminado o no
    *
    * */
    @Override
    public boolean isDeleted(String name) {
        UserDataMapper user = repository.getUserByName(name);
        return user.getDeletedAt() != null;
    }

    /*
    *
    * Devuelve un DTO de un usuario dado su nombre de usuario.
    *
    * */
    @Override
    public User getUserByName(String name) {
        UserDataMapper userDM = repository.getUserByName(name);
        return userFactory.create(userDM.getId(), userDM.getName(), userDM.getPassword(),
                userDM.getRol(), userDM.getCreatedAt(), userDM.getUpdatedAt(), userDM.getDeletedAt());
    }

    /*
    *
    * Crea la entidad de JPA y luego la persiste
    *
    * */
    @Override
    public void save(UserRequestModel requestModel) {
        UserDataMapper accountDataMapper = new UserDataMapper(requestModel.getName(), new BCryptPasswordEncoder().encode(requestModel.getPassword()));
        repository.save(accountDataMapper);
    }

    /*
    *
    * Obtiene una lista de entidades desde el repositorio de JPA que
    * luego se convierte e una lista de DTO de usuarios
    *
    * */
    @Override
    public List<User> getAll() {
        return convertToUsers(repository.findAll());
    }

    /*
    *
    * Obtiene todas las entidades de usuarios disponibles o sólo las eliminadas (dependiendo del booleano en el parámetro)
    * luego se convierte e una lista de DTO de usuarios
    *
    * */
    @Override
    public List<User> getAll(boolean onlyDeleted) {
        if(onlyDeleted){
            return convertToUsers(repository.getAllDeleted());
        }
        return convertToUsers(repository.getAllNotDeleted());
    }

    /*
    *
    * Recibe como método el nombre de usuario para realizar un borrado lógico
    *
    * */
    @Override
    public void delete(String name) {
        UserDataMapper user = repository.getUserByName(name);
        user.setDeletedAt(LocalDateTime.now());
        repository.save(user);
    }

    /*
    *
    * Convierte una lista de entidades en un lista de DTO
    *
    * */
    private List<User> convertToUsers(List<UserDataMapper> usersDataMapper){
        List<User> users = new ArrayList<>();
        for(UserDataMapper u : usersDataMapper) {
            users.add(userFactory.create(u.getId(), u.getName(), u.getPassword(),
                    u.getRol(), u.getCreatedAt(), u.getUpdatedAt(), u.getDeletedAt()));
        }
        return users;
    }

}

