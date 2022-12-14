package cl.binter.apiusers.usecase;

import cl.binter.apiusers.domain.dto.UserDTO;
import cl.binter.apiusers.domain.entities.User;
import cl.binter.apiusers.domain.entities.UserFactory;
import cl.binter.apiusers.domain.repository.UserRepository;
import cl.binter.apiusers.usecase.requests.UserRequestModel;
import cl.binter.apiusers.usecase.responses.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
*
* Clase que contiene la lógica de negocio de la aplicación
*
* */
@Service
@AllArgsConstructor
public class UserInteractor implements UserBoundary{

    private final UserRepository userRepository;
    private final UserPresenter userPresenter;
    private final UserFactory userFactory;

    /*
    *
    * Obtiene la solicitud, verifica si el nombre ya existe y si la contraseña a utilizar es correcta.
    * Luego manda la solicitud al repositorio para realizar la persistencia. Finalmente devuelve una
    * respuesta
    *
    * */
    @Override
    public UserResponse create(UserRequestModel requestModel) {

        if (userRepository.existsByName(requestModel.getName())) {
            return userPresenter.prepareConflictView("User already exists.");
        }
        User user = userFactory.create(requestModel.getName(), requestModel.getPassword());
        if (!user.passwordIsValid()) {
            return userPresenter.prepareConflictView("User password must have more than 5 characters.");
        }

        userRepository.save(requestModel);

        UserResponseModel accountResponseModel = new UserResponseModel("User " + user.getName() + " has been created.");
        return userPresenter.prepareSuccessView(accountResponseModel);
    }

    /*
    *
    * Obtiene la solicitud y el nombre de usuario autenticando. Verifica que la nueva contraseña a ingresar
    * sea válida. Verifica que el nuevo nombre de usuario no exista en la base de datos
    * (si el nombre de usuario no cambia o no se especifica no se realiza la comprobación anterior). Finalmente
    * si la operación se realiza de forma correcta devuelve una respuesta con un mensaje indicando que el
    * usuario se actualizó correctamente.
    *
    * */
    @Override
    public UserResponse update(UserRequestModel requestModel, String username) {

        User user = userFactory.create(requestModel.getName(), requestModel.getPassword());

        if(user.getPassword() != null){
            if (!user.passwordIsValid()) {
                return userPresenter.prepareConflictView("User password must have more than 5 characters.");
            }
        }

        if(user.getName() != null){
            if(!requestModel.getName().equals(username) && userRepository.existsByName(requestModel.getName())) {
                return userPresenter.prepareConflictView("User already exists.");
            }
            //Aqui se pueden agregar mas comprobaciones para el nombre de usuario
        }

        userRepository.update(requestModel, username);

        UserResponseModel accountResponseModel = new UserResponseModel("User " + username + " has been updated.");
        return userPresenter.prepareSuccessView(accountResponseModel);
    }

    /*
     *
     * Obtiene todos los usuarios registrados en el sistema utilizando el repositorio, realiza una validación
     * y deveulve una respuesta depenediendo si hay usuarios dentro de la base de datos
     *
     * */
    @Override
    public UserResponse getAll() {
        List<UserDTO> users = userRepository.getAll();
        if(users.isEmpty()) {
            return userPresenter.prepareNotFoundView("No users registered.");
        }
        AllUserResponseModel responseGetAll = new AllUserResponseModel(users);
        return userPresenter.prepareSuccessView(responseGetAll);
    }

    /*
     *
     * Obtiene todos los usuarios disponibles en el sistema utilizando el repositorio, realiza una validación
     * y deveulve una respuesta depenediendo si hay usuarios disponibles.
     *
     * */
    @Override
    public UserResponse getAllNotDeleted() {
        List<UserDTO> users = userRepository.getAll(false);
        if(users.isEmpty()) {
            return userPresenter.prepareNotFoundView("No users available.");
        }
        AllUserResponseModel responseGetAll = new AllUserResponseModel(users);
        return userPresenter.prepareSuccessView(responseGetAll);
    }

    /*
     *
     * Obtiene todos los usuarios eliminados del sistema utilizando el repositorio, realiza una validación
     * y deveulve una respuesta depenediendo si hay usuarios disponibles.
     *
     * */
    @Override
    public UserResponse getAllDeleted() {
        List<UserDTO> users = userRepository.getAll(true);
        if(users.isEmpty()) {
            return userPresenter.prepareNotFoundView("No users deleted.");
        }
        AllUserResponseModel responseGetAll = new AllUserResponseModel(users);
        return userPresenter.prepareSuccessView(responseGetAll);
    }

    /*
     *
     * Devuelve una respuesta que contiene el token y la hora actual, primero revisa si el usuario ha sido eliminado
     * y deveulve una respuesta depenediendo de esto
     *
     * */
    @Override
    public UserResponse responseToken(String jwt, String username) {
        if(userRepository.isDeleted(username)){
            return userPresenter.prepareNotFoundView("This user has been deleted.");
        }
        UserResponseModel responseModel = new UserResponseModel(jwt);
        return userPresenter.prepareSuccessView(responseModel);
    }

    /*
     *
     * Recibe un nombre por parametro y devuelve una respuesta dependiendo si el usuario fue eliminado de
     * la base de datos.
     *
     * */
    @Override
    public UserResponse getInfo(String username) {
        if(!userRepository.existsByName(username)) {
            return userPresenter.prepareNotFoundView("User does not exists.");
        }
        UserDTO user = userRepository.getUserDTO(username);
        UserResponseModel responseModel = new UserResponseModel(user);
        return userPresenter.prepareSuccessView(responseModel);
    }

    /*
    *
    * Recibe una solicitud y verifca que el usuario exista en la base de datos antes de eliminarlo
    * Finalmente devuelve una respuesta.
    *
    * */
    @Override
    public UserResponse delete(UserRequestModel requestModel) {
        if (!userRepository.existsByName(requestModel.getName())) {
            return userPresenter.prepareNotFoundView("User does not exists.");
        }
        userRepository.delete(requestModel.getName());

        UserResponseModel responseDelete = new UserResponseModel("The user " + requestModel.getName() + " has been deleted.");
        return userPresenter.prepareSuccessView(responseDelete);
    }
}
