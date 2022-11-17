package cl.binter.apiusers.integration;

import cl.binter.apiusers.domain.dto.UserDTO;
import cl.binter.apiusers.domain.entities.CommonUser;
import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
*
* Pruebas de integración realizadas con testcontainer para probar la interacción con la base de datos de la api.
*
* */
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    ObjectMapper objectMapper;
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

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    /*
    *
    * test en el que un usuario anonimo ingresa un nuevo usuario al sistema. Se verifica que
    * el usuario ingresado no sea nulo, que tenga un rol USER, que no tenga una fecha de eliminación
    * válida y que tenga el mismo nombre que se estableció en la solicitud del test.
    *
    * */
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

    /*
     *
     * test en el que un usuario del tipo USER ingresa un nuevo usuario al sistema. Se verifica que
     * el usuario ingresado no sea nulo, que tenga un rol USER, que no tenga una fecha de eliminación
     * válida y que tenga el mismo nombre que se estableció en la solicitud del test.
     *
     * */
    @Test
    @WithMockUser(username = "manu123",password = "secreto", roles = "USER")
    public void saveUserWithUser(){

        UserRequestModel request = new UserRequestModel("manu98", "secreto");
        userRepository.save(request);
        UserDTO user = userRepository.getUserDTO(request.getName());
        assertNotNull(user);
        assert(user.getName().equals(request.getName()));
        assertNull(user.getDeletedAt());
        assert(user.getRol().equals("USER"));

    }

    /*
     *
     * test en el que un administrador ingresa un nuevo usuario al sistema. Se verifica que
     * el usuario ingresado no sea nulo, que tenga un rol ADMIN, que no tenga una fecha de eliminación
     * válida y que tenga el mismo nombre que se estableció en la solicitud del test.
     *
     * */
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

    /*
     *
     * test en el que se elimina un usuario sistema. Posteriormente se obtiene este usuario
     * de la base de datos y se verifica que el usuario devuelto no sea nulo, que tenga una fecha de eliminación
     * válida y que tenga el mismo nombre que se estableció en la solicitud del test.
     *
     * */
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

    /*
     *
     * test en el que se actualiza un usuario en el sistema. Posteriormente se obtiene este usuario
     * de la base de datos y se verifica que el usuario devuelto no sea nulo, que tenga una fecha de actualización
     * válida, que su actual nombre de usuario y contraseña correspondan a los datos establecidos en el test pero que
     * sean diferentes a los datos antiguos establecidos en el test.
     *
     * */
    @Test
    @WithMockUser(username = "manu1",password = "secreto", roles = "USER")
    public void updateUser(){

        String oldUsername = "manu1";
        String oldPassword = "secreto";
        userRepository.save(new UserRequestModel(oldUsername, oldPassword));

        String newUsername = "manu1000";
        String newPassword = "secreto1";
        userRepository.update(new UserRequestModel(newUsername, newPassword), oldUsername);

        CommonUser user = (CommonUser) userRepository.getUserByName(newUsername);

        assertNotNull(user);
        assertNotNull(user.getUpdatedAt());
        assert(!user.getName().equals(oldUsername));
        assert(!new BCryptPasswordEncoder().matches(
                oldPassword,
                user.getPassword()
        ));
        assert(user.getName().equals(newUsername));
        assert(new BCryptPasswordEncoder().matches(
                newPassword,
                user.getPassword()
        ));

    }

    /*
     *
     * test en el que se actualiza un usuario en el sistema, pero esta vez simplemente se actualiza
     * el nombre. Posteriormente se obtiene este usuario de la base de datos y se verifica que el usuario
     * devuelto no sea nulo, que tenga una fecha de actualización válida, que su actual nombre de usuario
     * corresponda a los datos establecidos en el test pero que
     * sean diferentes a los datos antiguos establecidos en el test.
     *
     * */
    @Test
    @WithMockUser(username = "manu2",password = "secreto", roles = "USER")
    public void updateUserOnlyUsername(){

        String oldUsername = "manu2";
        String password = "secreto";
        userRepository.save(new UserRequestModel(oldUsername, password));

        String newUsername = "manu2000";
        userRepository.update(new UserRequestModel(newUsername, null), oldUsername);

        CommonUser user = (CommonUser) userRepository.getUserByName(newUsername);

        assertNotNull(user);
        assertNotNull(user.getUpdatedAt());
        assert(!user.getName().equals(oldUsername));
        assert(user.getName().equals(newUsername));
        assert(new BCryptPasswordEncoder().matches(
                password,
                user.getPassword()
        ));

    }

    /*
     *
     * test en el que se actualiza un usuario en el sistema, pero esta vez simplemente se actualiza
     * su contraseña. Posteriormente se obtiene este usuario de la base de datos y se verifica que el usuario
     * devuelto no sea nulo, que tenga una fecha de actualización válida, que su actual contraseña
     * corresponda a los datos establecidos en el test pero que
     * sean diferentes a los datos antiguos establecidos en el test.
     *
     * */
    @Test
    @WithMockUser(username = "manu3",password = "secreto", roles = "USER")
    public void updateUserOnlyPassword(){

        String username = "manu3";
        String oldPassword = "secreto";
        userRepository.save(new UserRequestModel(username, oldPassword));

        String newPassword = "secreto1";
        userRepository.update(new UserRequestModel(null, newPassword), username);

        CommonUser user = (CommonUser) userRepository.getUserByName(username);

        assertNotNull(user);
        assertNotNull(user.getUpdatedAt());
        assert(user.getName().equals(username));
        assert(!new BCryptPasswordEncoder().matches(
                oldPassword,
                user.getPassword()
        ));
        assert(new BCryptPasswordEncoder().matches(
                newPassword,
                user.getPassword()
        ));

    }

    /*
     *
     * Test en el que se elimina un usuario que no existe en la base de datos, al invocar el metodo
     * delete del repositorio el programa arrojará una excepción debido a que se intentará traer
     * desde la base de datos un usuario inexsitente.
     *
     * */
    @Test
    @WithMockUser(username = "manu3",password = "secreto", roles = "ADMIN")
    public void deleteUserThatDontExists(){

        UserDTO userDTO = null;
        UserRequestModel request = new UserRequestModel();
        request.setName("manu9999");

        try{
            userRepository.delete(request.getName());
            userDTO = userRepository.getUserDTO(request.getName());
            assertNotNull(userDTO);
        }catch(NullPointerException e){
            assertNull(userDTO);
        }
    }


}