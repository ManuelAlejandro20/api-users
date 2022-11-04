package cl.binter.apiusers.unit;

import cl.binter.apiusers.domain.dto.UserDTO;
import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.usecase.UserPresenter;
import cl.binter.apiusers.usecase.responses.AllUserResponseModel;
import cl.binter.apiusers.usecase.responses.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "manu123",password = "secreto", roles = {"ADMIN"})
public class ApiUsersApplicationAdminTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserPresenter userPresenter;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
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

        AllUserResponseModel allUserResponseModel = new AllUserResponseModel(users);
        allUserResponseModel.setCurrentTime(nowString);
        when(userRepository.getAll()).thenReturn(users);
        when(userPresenter.prepareSuccessView(any(UserResponse.class))).thenReturn(allUserResponseModel);

        mvc.perform(get("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTime").value(nowString))
                .andExpect(jsonPath("$.users", hasSize(3)));
    }
}