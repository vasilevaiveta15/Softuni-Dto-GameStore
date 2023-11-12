package gamesystem.services.impl;

import gamesystem.AuthenticationContext;
import gamesystem.models.Game;
import gamesystem.models.User;
import gamesystem.repositories.GameRepository;
import gamesystem.services.api.GameService;
import gamesystem.services.api.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

public class GameServiceImpl implements GameService {
    private GameRepository gameRepository;
    private Validator validator;
    private AuthenticationContext authenticationContext;
    private UserService userService;


    public GameServiceImpl(GameRepository gameRepository,AuthenticationContext authenticationContext,
                           UserService userService) {
        this.gameRepository = gameRepository;
        this.getValidator();
        this.authenticationContext=authenticationContext;
        this.userService=userService;
    }

    @Override
    public void addGame(Game game) {
        if(this.checkIfGameExist(game.getTitle())){
            throw new IllegalArgumentException("Game already exist!");
        }
        Set<ConstraintViolation<Game>> constraintViolations = validator.validate(game);
        if (constraintViolations.size() == 0) {
            this.gameRepository.save(game);
        }else{
            for (ConstraintViolation<Game> constraintViolation : constraintViolations) {
                System.out.println(constraintViolation.getMessage());
            }
        }

    }
    @Override
    public void removeGame(Long id){
        this.gameRepository.deleteById(id);
    }


    private void getValidator(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public boolean checkIfGameExist(String title){
        Game game=this.gameRepository.findByTitle(title);
        return game !=null;
    }

    @Override
    public Game findGameByTitle(String title) {
        return this.gameRepository.findByTitle(title);
    }

    @Override
    public StringBuilder allGame(){
        StringBuilder str = new StringBuilder();
        List<Game> gameList = this.gameRepository.findAll();
        for(Game game:gameList){
            str.append(game.getTitle()).append(" ");
            str.append(game.getPrice());
            str.append(System.lineSeparator());
        }
        return str;
    }

    @Override
    public String detailGame(String title){
        Game game = this.gameRepository.findByTitle(title);
        return String.format("Title: %s%nPrice: %.2f%nDescription: %s%nRelease date: %s"
                ,game.getTitle(),game.getPrice(),game.getDescription());
    }

    @Override
    public StringBuilder ownedGames(){
        User user= this.userService.getUserByEmail(this.authenticationContext.getLoggedInUser().getEmail());
        StringBuilder str= new StringBuilder();
        for(Game game:user.getGames()){
            str.append(game.getTitle()).append(System.lineSeparator());
        }
        return str;
    }

}