package gamesystem.services.api;

import gamesystem.models.Game;

import java.util.Collection;

public interface OrderService {
    void buyItems(Collection<Game> product, String email);
}
