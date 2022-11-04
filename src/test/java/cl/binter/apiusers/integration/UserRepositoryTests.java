package cl.binter.apiusers.integration;

import cl.binter.apiusers.domain.dto.UserDTO;
import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11-alpine")
            .withDatabaseName("bdapi")
            .withPassword("postgres")
            .withUsername("admin");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Test
    @WithAnonymousUser
    public void saveUserWithAnon(){

        UserRequestModel request = new UserRequestModel("manu99", "secreto");
        userRepository.save(request);
        UserDTO user = userRepository.getUserDTO(request.getName());
        assertNotNull(user);
        assert(user.getName().equals(request.getName()));
        assertNull(user.getDeletedAt());
        assert(user.getRol().equals("USER"));

    }

    @Test
    @WithMockUser(username = "manu123",password = "secreto", roles = {"USER"})
    public void saveUserWithUser(){

        UserRequestModel request = new UserRequestModel("manu98", "secreto");
        userRepository.save(request);
        UserDTO user = userRepository.getUserDTO(request.getName());
        assertNotNull(user);
        assert(user.getName().equals(request.getName()));
        assertNull(user.getDeletedAt());
        assert(user.getRol().equals("USER"));

    }

    @Test
    @WithMockUser(username = "manu123",password = "secreto", roles = {"ADMIN"})
    public void saveUserWithAdmin(){

        UserRequestModel request = new UserRequestModel("manu97", "secreto");
        userRepository.save(request);
        UserDTO user = userRepository.getUserDTO(request.getName());
        assertNotNull(user);
        assert(user.getName().equals(request.getName()));
        assertNull(user.getDeletedAt());
        assert(user.getRol().equals("ADMIN"));

    }

    @Test
    @WithMockUser(username = "manu123",password = "secreto", roles = {"ADMIN"})
    public void deleteUser(){

        UserRequestModel request = new UserRequestModel("manu96", "secreto");
        userRepository.save(request);
        userRepository.delete(request.getName());
        UserDTO user = userRepository.getUserDTO(request.getName());
        assertNotNull(user);
        assert(user.getName().equals(request.getName()));
        assertNotNull(user.getDeletedAt());

    }


}