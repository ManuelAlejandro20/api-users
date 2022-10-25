package cl.binter.apiusers.infrastructure.providers.db;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cl.binter.apiusers.infrastructure.providers.db.model.UserDataMapper;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DbUserRepository extends JpaRepository<UserDataMapper, String> {
    @Query(value =  "SELECT u.* " +
                    "FROM users AS u " +
                    "WHERE u.name = ?1", nativeQuery = true)
    UserDataMapper getUserByName(String name);

    @Query(value =  "SELECT * " +
                    "FROM users " +
                    "WHERE deleted_at IS NULL ", nativeQuery = true)
    List<UserDataMapper> getAllNotDeleted();

    @Query(value =  "SELECT u.* " +
                    "FROM users AS u " +
                    "WHERE u.deleted_at IS NOT NULL ", nativeQuery = true)
    List<UserDataMapper> getAllDeleted();

    @Modifying
    @Transactional
    @Query(value =  "UPDATE users " +
                    "SET deleted_at = ?2 " +
                    "WHERE name = ?1", nativeQuery = true)
    void logicalDeleteByName(String name, LocalDateTime now);


}
