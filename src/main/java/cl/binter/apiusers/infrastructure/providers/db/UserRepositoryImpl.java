package cl.binter.apiusers.infrastructure.providers.db;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cl.binter.apiusers.domain.entities.CommonUser;
import cl.binter.apiusers.domain.entities.User;
import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.infrastructure.providers.db.model.UserDataMapper;

import cl.binter.apiusers.usecase.requests.UserRequestModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository{

    private final DbUserRepository repository;

    @Override
    public boolean existsByName(String name) {
        return repository.getUserByName(name) != null;
    }

    @Override
    public void save(UserRequestModel requestModel) {
        UserDataMapper accountDataMapper = new UserDataMapper(requestModel.getName(), requestModel.getPassword());
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
    public void delete(UserRequestModel requestModel) {
        repository.logicalDeleteByName(requestModel.getName(), LocalDateTime.now());
    }

    private List<User> convertToUsers(List<UserDataMapper> usersDataMapper){
        List<User> users = new ArrayList<>();
        for(UserDataMapper u : usersDataMapper) {
            users.add(new CommonUser(u.getId(), u.getName(), u.getPassword(), u.getCreatedAt(), u.getUpdatedAt(), u.getDeletedAt()));
        }
        return users;
    }

}

