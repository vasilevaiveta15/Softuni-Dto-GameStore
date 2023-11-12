package gamesystem.services.api;

import gamesystem.dtos.UserDto;
import gamesystem.models.Game;
import gamesystem.models.User;

import java.util.Collection;
import java.util.Set;

public interface UserService {
    void registerUser(User user, String confirmPassword);

    boolean checkIfUserExist(String email);

    UserDto getUserDtoByEmail(String email);

    User getUserByEmail(String email);


    void buyItems(Collection<Game> product, String email);

}
