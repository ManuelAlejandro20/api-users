package cl.binter.apiusers.infrastructure.providers.db;

import java.util.ArrayList;
import java.util.List;

import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.infrastructure.providers.db.model.UserDataMapper;
import cl.binter.apiusers.usecase.requests.UserDSRequestModel;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository{

    private final DbUserRepository repository;

    @Override
    public boolean existsByName(String name) {
        return repository.existsById(name);
    }

    @Override
    public void save(UserDSRequestModel requestModel) {
        UserDataMapper accountDataMapper = new UserDataMapper(requestModel.getName(), requestModel.getPassword(), requestModel.getCreationTime());
        repository.save(accountDataMapper);
    }

    @Override
    public List<String> getAll() {
        List<UserDataMapper> users = repository.findAll();
        List<String> usersString = new ArrayList<String>();
        for(UserDataMapper u : users) {
            usersString.add(u.getName());
        }
        return usersString;
    }

    @Override
    public void delete(String name) {
        repository.deleteById(name);
    }

}

