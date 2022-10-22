package cl.binter.apiusers.infrastructure.providers.db;

import org.springframework.stereotype.Repository;

import cl.binter.apiusers.infrastructure.providers.db.model.UserDataMapper;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DbUserRepository extends JpaRepository<UserDataMapper, String> {

}
