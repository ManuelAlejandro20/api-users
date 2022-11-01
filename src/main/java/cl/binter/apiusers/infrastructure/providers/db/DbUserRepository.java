package cl.binter.apiusers.infrastructure.providers.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import cl.binter.apiusers.infrastructure.providers.db.model.UserDataMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface DbUserRepository extends JpaRepository<UserDataMapper, String> {
    @Query(value =  "SELECT u " +
                    "FROM UserDataMapper u " +
                    "WHERE u.name = ?1")
    UserDataMapper getUserByName(String name);

    @Query(value =  "SELECT u " +
                    "FROM UserDataMapper u " +
                    "WHERE u.deletedAt IS NULL ")
    List<UserDataMapper> getAllNotDeleted();

    @Query(value =  "SELECT u " +
                    "FROM UserDataMapper u " +
                    "WHERE u.deletedAt IS NOT NULL ")
    List<UserDataMapper> getAllDeleted();


}
