package cl.binter.apiusers.configuration;

import cl.binter.apiusers.domain.entities.CommonUserFactory;
import cl.binter.apiusers.domain.entities.UserFactory;
import cl.binter.apiusers.infrastructure.UserResponseFormatter;
import cl.binter.apiusers.usecase.UserPresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiUsersConfiguration {

    @Bean
    public UserFactory beanUserFactory() {
        return new CommonUserFactory();
    }

    @Bean
    public UserPresenter beanUserPresenter() {
        return new UserResponseFormatter();
    }
}
