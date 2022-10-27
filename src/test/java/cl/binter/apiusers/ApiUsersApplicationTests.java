package cl.binter.apiusers;

import cl.binter.apiusers.infrastructure.controller.UserController;
import cl.binter.apiusers.usecase.JwtUtilService;
import cl.binter.apiusers.usecase.UserBoundary;
import cl.binter.apiusers.usecase.UserDS;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import cl.binter.apiusers.usecase.responses.UserResponse;
import cl.binter.apiusers.usecase.responses.AllUserResponseModel;
import cl.binter.apiusers.usecase.responses.UserResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class ApiUsersApplicationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserBoundary userBoundary;

    @MockBean
    private UserDS userDS;

    @MockBean
    private JwtUtilService jwtService;

    @Test
    public void shouldCreateMockMvc(){
        assertNotNull(mvc);
    }

/*    @Test
    public void getAllUsers() throws Exception {
        LocalDateTime responseTime = LocalDateTime.now();
        String time = responseTime.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        String[] users = {"Noemi", "Manuel"};
        UserResponse ur = new AllUserResponseModel(List.of(users), time);
        when(userBoundary.getAll()).thenReturn(ur);
        mvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(2)))
                .andExpect(jsonPath("$.time").value(time))
                .andDo(print());
    }*/

    @Test
    public void registerNewUser() throws Exception {
        LocalDateTime responseTime = LocalDateTime.now();
        String time = responseTime.format(DateTimeFormatter.ofPattern("hh:mm:ss"));
        UserResponse ur = new UserResponseModel("Manu");
        ur.setCurrentTime(time);
        when(userBoundary.create(any(UserRequestModel.class))).thenReturn(ur);

        mvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Manu\",\"password\":\"987654321\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentTime").value(time))
                .andDo(print());
    }




}
