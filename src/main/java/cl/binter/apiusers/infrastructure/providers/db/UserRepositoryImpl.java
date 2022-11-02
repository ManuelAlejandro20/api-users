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

@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository{

    private final DbUserRepository repository;
    private final UserFactory userFactory;

    @Override
    public boolean existsByName(String name) {
        return repository.getUserByName(name) != null;
    }

    @Override
    public boolean isDeleted(String name) {
        UserDataMapper user = repository.getUserByName(name);
        return user.getDeletedAt() != null;
    }

    @Override
    public User getUserByName(String name) {
        UserDataMapper userDM = repository.getUserByName(name);
        return userFactory.create(userDM.getId(), userDM.getName(), userDM.getPassword(),
                userDM.getRol(), userDM.getCreatedAt(), userDM.getUpdatedAt(), userDM.getDeletedAt());
    }

    @Override
    public void save(UserRequestModel requestModel) {
        UserDataMapper accountDataMapper = new UserDataMapper(requestModel.getName(), new BCryptPasswordEncoder().encode(requestModel.getPassword()));
        repository.save(accountDataMapper);
    }

    @Override
    public List<User> getAll() {
        return convertToUsers(repository.findAll());
    }

    @Override
    public List<User> getAll(boolean onlyDeleted) {
        if(onlyDeleted){
            return convertToUsers(repository.getAllDeleted());
        }
        return convertToUsers(repository.getAllNotDeleted());
    }

    @Override
    public void delete(String name) {
        UserDataMapper user = repository.getUserByName(name);
        user.setDeletedAt(LocalDateTime.now());
        repository.save(user);
    }

    private List<User> convertToUsers(List<UserDataMapper> usersDataMapper){
        List<User> users = new ArrayList<>();
        for(UserDataMapper u : usersDataMapper) {
            users.add(userFactory.create(u.getId(), u.getName(), u.getPassword(),
                    u.getRol(), u.getCreatedAt(), u.getUpdatedAt(), u.getDeletedAt()));
        }
        return users;
    }

}

