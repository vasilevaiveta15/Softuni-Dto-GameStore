package gamesystem.controllers;

import gamesystem.AuthenticationContext;
import gamesystem.models.User;
import gamesystem.services.api.UserService;

public class UserController {
    private UserService userService;
    private AuthenticationContext context;

    public UserController(UserService userService, AuthenticationContext context) {
        this.userService = userService;
        this.context = context;
    }

    public String register(String email, String password, String confirmPassword, String fullName){
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(password);
        try{
            this.userService.registerUser(user, confirmPassword);
        } catch (Exception e){
            return e.getMessage();
        }
       return String.format("%s registered successfully!", fullName);
    }

    public String login(String email, String password){
        if(!this.userService.checkIfUserExist(email)){
            return "User does not exist!";
        }
        this.context.setLoggedInUser(this.userService.getUserDtoByEmail(email));
        return "User logged in successfully!";
    }

    public String logout(){
        return "User successfully logged out!";
    }

}
