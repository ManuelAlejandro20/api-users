package cl.binter.apiusers.unit;

import cl.binter.apiusers.domain.dto.UserDTO;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 *
 * Tests que validan las rutas a las que puede acceder un usuario de tipo user.
 *
 * */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "manu123",password = "secreto", roles = "USER")
public class UserTests {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserRepository userRepository;

    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    /*
    *
    * Test que verifica los datos devueltos por un usuario autenticado al realizar la llamada /api/info
    * Se mockea el método existsByName() para que se estblezca que el usuario
    * que intenta ver su información si exista. Además se devuelve un usuario ya
    * declarado al acceder al método getUserDTO(). Se espera un status HTTP 200 y que el tiempo
     * de ejecución junto con la respuesta devuelta por la api sean los mismos que la respuesta
     * establecida en el test.
    *
    * */
    @Test
    public void getInfo() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        String nowString = now.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        UserDTO user = new UserDTO("manu123", "USER", now, null, null);
        UserResponseModel response = new UserResponseModel(user);
        response.setCurrentTime(nowString);

        when(userRepository.existsByName(any(String.class))).thenReturn(true);
        when(userRepository.getUserDTO(any(String.class))).thenReturn(user);

        mvc.perform(get("/api/info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTime").value(response.getCurrentTime()))
                .andExpect(jsonPath("$.response.name").value(user.getName()));
    }

    /*
    *
    * test que valida la actualización de los datos de un usuario. Se mockea el método existsByName()
    * para que se establezca que el nuevo nombre a ingresar no exista en la base de datos y tambien
    * se ignora el método update() del repositorio. Se espera un status HTTP 200 y que el tiempo
    * de ejecución junto con la respuesta devuelta por la api sean los mismos que la respuesta
    * establecida en el test.
    *
    * */
    @Test
    public void updateUser() throws Exception {

        UserRequestModel request = new UserRequestModel("manu99", "987654321");
        UserResponseModel response = new UserResponseModel("User manu123 has been updated.");
        LocalDateTime responseTime = LocalDateTime.now();
        String time = responseTime.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        response.setCurrentTime(time);

        when(userRepository.existsByName(any(String.class))).thenReturn(false);
        doNothing().when(userRepository).update(isA(UserRequestModel.class), isA(String.class));

        mvc.perform(post("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTime").value(response.getCurrentTime()))
                .andExpect(jsonPath("$.response").value(response.getResponse()));
    }

    /*
    *
    * Test en el que se intenta actualizar un usuario pero con una contraseña invalida. La respuesta
    * esperada es un HTTP 409 (Conflict)
    *
    * */
    @Test
    public void updateUserShortPassword() throws Exception {

        UserRequestModel request = new UserRequestModel("manu99", "9");

        mvc.perform(post("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    /*
    *
    * test en el que se intenta actualizar un usuario pero con un nombre
    * que ya exista previamente en la base de datos. Se mockea el método existsByName() para
    * que nos indique que efectivamente ese usuario ya existe. Se verifica que la respuesta
    * devuelta sea un HTTP 409 (Conflict).
    *
    * */
    @Test
    public void updateUserAlreadyExists() throws Exception {

        UserRequestModel request = new UserRequestModel("manu99", "987654321");
        when(userRepository.existsByName(any(String.class))).thenReturn(true);


        mvc.perform(post("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    /*
    *
    * test que intenta actualizar solamente la contraseña del usuario, sin embargo igual se
    * entrega el nombre de usuario en la solicitud. Se mockea el método existsByName()
     * para que se establezca que el nuevo nombre a ingresar exista en la base de datos
     * (dado a que corresponde al del mismo usuario a actualizar), sin embargo debido
     * a que mantiene el nombre de usuario esta verificación se ignora. Tambien
     * se ignora el método update() del repositorio. Se espera un status HTTP 200 y que el tiempo
     * de ejecución junto con la respuesta devuelta por la api sean los mismos que la respuesta
     * establecida en el test.
    *
    * */
    @Test
    public void updateUserKeepUsername() throws Exception {

        UserRequestModel request = new UserRequestModel("manu123", "987654321");
        UserResponseModel response = new UserResponseModel("User manu123 has been updated.");
        LocalDateTime responseTime = LocalDateTime.now();
        String time = responseTime.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        response.setCurrentTime(time);

        when(userRepository.existsByName(any(String.class))).thenReturn(true);
        doNothing().when(userRepository).update(isA(UserRequestModel.class), isA(String.class));

        mvc.perform(post("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTime").value(response.getCurrentTime()))
                .andExpect(jsonPath("$.response").value(response.getResponse()))
                .andDo(print());
    }

    /*
    *
    * Test que llama a una ruta de un usuario autenticado siendo un usuario anonimo.
    * En este caso se espera una respuesta HTTP 403 (Forbidden).
    *
    * */
    @Test
    @WithAnonymousUser
    public void getInfoAnonymous() throws Exception {
        mvc.perform(get("/api/info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
