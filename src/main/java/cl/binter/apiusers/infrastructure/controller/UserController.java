package cl.binter.apiusers.infrastructure.controller;

import cl.binter.apiusers.usecase.JwtUtilService;
import cl.binter.apiusers.usecase.responses.UserResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import cl.binter.apiusers.usecase.UserBoundary;
import cl.binter.apiusers.usecase.requests.UserRequestModel;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final UserBoundary userBoundary;
    UserDetailsService userDetailsService;
    private JwtUtilService jwtUtilService;
    private AuthenticationManager authenticationManager;

    /*
    *
    * Autentica al usuario y devuelve una respuesta con el token y la hora de ejecución
    *
    * */
    @PostMapping("/authenticate")
    public UserResponse authenticate(@RequestBody UserRequestModel requestModel){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestModel.getName(),
                        requestModel.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(requestModel.getName());
        String jwt = jwtUtilService.generateToken(userDetails);
        return userBoundary.responseToken(jwt, requestModel.getName());

    }

    /*
    *
    * Recibe una solicitud con el nombre del usuario y contraseña, devuelve una respuesta indicando
    * que el usuario fue agregado correctamente o un mensaje con el error correspondiente.
    *
    * */
    @PostMapping("/register")
    public UserResponse create(@RequestBody UserRequestModel requestModel){
        return userBoundary.create(requestModel);
    }

    /*
    *
    * Recibe una solicitud con el nombre o contraseña de un usuario, devuelve
    * una respuesta indicando que la operación se realizó de forma correcta
    *
    * */
    @PostMapping("/update")
    public UserResponse update(@RequestBody UserRequestModel requestModel){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userBoundary.update(requestModel, auth.getName());
    }

    /*
    *
    * Recibe una solicitud con el nombre de usuario y devuelve una respuesta indicando el error o un mensaje indicando que el usuario fue eliminado
    * correctamente.
    *
    * */
    @PostMapping("/users/delete")
    public UserResponse delete(@RequestBody UserRequestModel requestModel){
        return userBoundary.delete(requestModel);
    }

    /*
    *
    * Devuelve como respuesta todos los usuarios disponibles en base de datos.
    *
    * */
    @GetMapping("/users/all")
    public UserResponse getAll(){
        return userBoundary.getAll();
    }

    /*
     *
     * Obtiene el usuario actualmente autenticado y devuelve una respuesta indicando toda su información
     *
     * */
    @GetMapping("/info")
    public UserResponse getInfo(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userBoundary.getInfo(auth.getName());
    }

    /*
    *
    * Devuelve una respuesta con todos los usuarios que no han sido eliminados del sistema
    *
    * */
    @GetMapping("/users/available")
    public UserResponse getAllNotDeleted(){
        return userBoundary.getAllNotDeleted();
    }

    /*
     *
     * Devuelve una respuesta con todos los usuarios que han sido eliminados del sistema
     *
     * */
    @GetMapping("/users/deleted")
    public UserResponse getAllDeleted(){
        return userBoundary.getAllDeleted();
    }

}

