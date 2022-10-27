package cl.binter.apiusers.configuration;

import cl.binter.apiusers.filters.JwtRequestFilter;
import cl.binter.apiusers.usecase.JwtUtilService;
import cl.binter.apiusers.usecase.UserDS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDS userDS;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/v3/api-docs").permitAll()
            .antMatchers("/configuration/ui").permitAll()
            .antMatchers("/swagger-resources/**").permitAll()
            .antMatchers("/configuration/security").permitAll()
            .antMatchers("/swagger-ui.html").permitAll()
            .antMatchers("/swagger-ui/*").permitAll()
            .antMatchers("/webjars/**").permitAll()
            .antMatchers("/v3/**").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/users/**").hasRole("ADMIN")
            .antMatchers("/api/info").hasAnyRole("ADMIN", "USER")
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDS);
        /*        auth
            .inMemoryAuthentication()
            .withUser("manu").password("{noop}" + "secreto").roles("ADMIN");*/
    }


}
