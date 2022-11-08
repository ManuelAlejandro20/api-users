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

    @Test
    public void userAlreadyExists() throws Exception {

        UserRequestModel request = new UserRequestModel("Manu", "987654321");
        when(userRepository.existsByName(any(String.class))).thenReturn(true);

        mvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

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
