package cl.binter.apiusers.unit;

import cl.binter.apiusers.domain.entities.User;
import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import cl.binter.apiusers.usecase.responses.UserResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
*
* Tests que validan los métodos que se relacionan con el registro de nuevos usuarios
*
* */
@SpringBootTest
@AutoConfigureMockMvc
public class NewUserTests {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private User user;

    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    /*
    *
    * Test en el que se busca ingresar un nuevo usuario. Mockea el método existsByName() del repositorio para indicarnos que el usuario
    * no existe en la base de datos, tambien ignora el metodo save(). Se espera un status HTTP 200 y que el tiempo
     * de ejecución junto con la respuesta devuelta por la api sean los mismos que la respuesta
     * establecida en el test.
    *
    * */
    @Test
    public void registerNewUser() throws Exception {

        UserRequestModel request = new UserRequestModel("Manu", "987654321");
        UserResponseModel ur = new UserResponseModel("User Manu has been created.");
        LocalDateTime responseTime = LocalDateTime.now();
        String time = responseTime.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        ur.setCurrentTime(time);

        when(userRepository.existsByName(any(String.class))).thenReturn(false);
        doNothing().when(userRepository).save(isA(UserRequestModel.class));

        mvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(ur.getResponse()))
                .andExpect(jsonPath("$.currentTime").value(ur.getCurrentTime()));
    }

    /*
    *
    * Test que busca ingresar un usuario que ya existe Test. Mockea el método existsByName() del
    * repostiorio para indicarnos que el usuario que se quiere registrar ya existe.
    * Se verifica que se reciba una respuesta HTTP 409 (conflict).
    *
    * */
    @Test
    public void userAlreadyExists() throws Exception {

        UserRequestModel request = new UserRequestModel("Manu", "987654321");
        when(userRepository.existsByName(any(String.class))).thenReturn(true);

        mvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    /*
    *
    * test que busca ingresar un nuevo usuario pero con una constraseña invalida. Mockea el método existByName() para indicarnos que el usuario
    * que se quiere agregar no existe en la base de datos y se mockea passwordIsValid() de
    * la interface User para que devuelva que la contraseña no sea válida. A raiz de esto
    * último se espera que la respuesta recibida de la api sea un HTTP 409 (Conflict)
    *
    * */
    @Test
    public void shortPassword() throws Exception {

        UserRequestModel request = new UserRequestModel("Manu", "1");
        when(userRepository.existsByName(any(String.class))).thenReturn(false);
        when(user.passwordIsValid()).thenReturn(false);

        mvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

}
