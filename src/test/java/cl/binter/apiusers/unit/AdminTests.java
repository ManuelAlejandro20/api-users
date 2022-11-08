package cl.binter.apiusers.unit;

import cl.binter.apiusers.domain.dto.UserDTO;
import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import cl.binter.apiusers.usecase.responses.AllUserResponseModel;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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
@WithMockUser(username = "manu123",password = "secreto", roles = "ADMIN")
public class AdminTests {
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
    public void getAll() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        String nowString = now.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        List<UserDTO> users = new ArrayList<>();
        UserDTO user1 = new UserDTO("manu123", "USER",now, null, null);
        UserDTO user2 = new UserDTO("noemi1", "ADMIN",now, null, null);
        UserDTO user3 = new UserDTO("javi890", "USER",now, null, null);
        users.add(user1);
        users.add(user2);
        users.add(user3);

        AllUserResponseModel response = new AllUserResponseModel(users);
        response.setCurrentTime(nowString);

        when(userRepository.getAll()).thenReturn(users);

        mvc.perform(get("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTime").value(response.getCurrentTime()))
                .andExpect(jsonPath("$.users", hasSize(3)))
                .andExpect(jsonPath("$.users[0].name").value(users.get(0).getName())) //Verifica que existe manu123
                .andDo(print());
    }

    @Test
    public void deleteUser() throws Exception {

        UserRequestModel request = new UserRequestModel();
        request.setName("manu123");
        UserResponseModel response = new UserResponseModel("The user " + request.getName() + " has been deleted.");
        LocalDateTime responseTime = LocalDateTime.now();
        String time = responseTime.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        response.setCurrentTime(time);

        when(userRepository.existsByName(any(String.class))).thenReturn(true);
        doNothing().when(userRepository).delete(isA(String.class));

        mvc.perform(post("/api/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(response.getResponse()))
                .andExpect(jsonPath("$.currentTime").value(response.getCurrentTime()));
    }

    @Test
    public void userThatDoesntExists() throws Exception {

        UserRequestModel request = new UserRequestModel();
        request.setName("manu123");

        when(userRepository.existsByName(any(String.class))).thenReturn(false);

        mvc.perform(post("/api/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    public void deleteUserAnonymous() throws Exception {

        UserRequestModel request = new UserRequestModel();
        request.setName("manu123");

        mvc.perform(post("/api/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }



}
