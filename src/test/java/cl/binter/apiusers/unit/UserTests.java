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

    @Test
    public void updateUserShortPassword() throws Exception {

        UserRequestModel request = new UserRequestModel("manu99", "9");

        mvc.perform(post("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

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

    @Test
    @WithAnonymousUser
    public void getInfoAnonymous() throws Exception {
        mvc.perform(get("/api/info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
