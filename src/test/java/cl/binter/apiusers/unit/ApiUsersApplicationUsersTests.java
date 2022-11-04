package cl.binter.apiusers.unit;

import cl.binter.apiusers.domain.entities.CommonUser;
import cl.binter.apiusers.domain.entities.User;
import cl.binter.apiusers.usecase.UserBoundary;
import cl.binter.apiusers.usecase.UserPresenter;
import cl.binter.apiusers.usecase.responses.UserResponseModel;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "manu123",password = "secreto", roles = {"USER"})
public class ApiUsersApplicationUsersTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserPresenter userPresenter;
    @MockBean
    private UserBoundary userBoundary;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getInfo() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String nowString = now.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        User user = new CommonUser(1, "manu123", "secreto", "USER",now, null, null);
        UserResponseModel infoResponse = new UserResponseModel(user);
        infoResponse.setCurrentTime(nowString);
        when(userBoundary.getInfo(any(String.class))).thenReturn(infoResponse);

        mvc.perform(get("/api/info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTime").value(nowString))
                .andExpect(jsonPath("$.response.name").value("manu123"));
    }
}
