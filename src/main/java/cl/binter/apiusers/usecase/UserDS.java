package cl.binter.apiusers.usecase;

import cl.binter.apiusers.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
*
* UserDetailsService personalizado, indica como se obtiene el usuario a autenticar
*
* */
@AllArgsConstructor
@Service
public class UserDS implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(userRepository.existsByName(username)){
            cl.binter.apiusers.domain.entities.User user = userRepository.getUserByName(username);
            User.UserBuilder userBuilder = User.withUsername(username);
            userBuilder.password(user.getPassword()).roles(user.getRol());
            return userBuilder.build();
        }else {
            throw new UsernameNotFoundException(username);
        }
    }
}
