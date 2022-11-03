package cl.binter.apiusers.configuration;

import cl.binter.apiusers.domain.entities.UserDTOFactory;
import cl.binter.apiusers.domain.entities.UserFactory;
import cl.binter.apiusers.infrastructure.UserResponseFormatter;
import cl.binter.apiusers.usecase.UserPresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
* Clase que provee dos métodos bean que serán utilizados cuando inicie la aplicación.
* */
@Configuration
public class ApiUsersConfiguration {

    @Bean
    public UserFactory beanUserFactory() {
        return new UserDTOFactory();
    }

    @Bean
    public UserPresenter beanUserPresenter() {
        return new UserResponseFormatter();
    }
}
