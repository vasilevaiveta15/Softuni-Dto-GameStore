package gamesystem.services.api;

import gamesystem.models.Game;


public interface GameService {
    void addGame(Game game);

    void removeGame(Long id);

    boolean checkIfGameExist(String title);

    Game findGameByTitle(String title);

    StringBuilder allGame();

    String detailGame(String title);

    StringBuilder ownedGames();

}
