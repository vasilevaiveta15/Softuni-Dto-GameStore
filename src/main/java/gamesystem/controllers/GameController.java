package gamesystem.controllers;

import gamesystem.models.Game;
import gamesystem.services.api.GameService;

import java.math.BigDecimal;
import java.util.Date;

public class GameController {
    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    public String add(String title, BigDecimal price, BigDecimal size,
                      String trailer, String thumbnailUrl, String description, Date releaseDate) {
        Game game = new Game();
        game.setTitle(title);
        game.setPrice(price);
        game.setSize(size);
        game.setTrailer(trailer);
        game.setThumbnailUrl(thumbnailUrl);
        try {
            this.gameService.addGame(game);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Game added successfully!";
    }

    public void delete(Long id) {

        this.gameService.removeGame(id);
        System.out.println("Successfully remove game!");
    }

    public String all(){
      return "All Games\n"+gameService.allGame();
    }

    public void detail(String title){
        Game game = this.gameService.findGameByTitle(title);
        System.out.println(game.toString());
    }

    public void owned(){
        this.gameService.ownedGames();
    }
}
