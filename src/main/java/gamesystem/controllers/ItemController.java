package gamesystem.controllers;

import gamesystem.AuthenticationContext;
import gamesystem.Cart;
import gamesystem.models.Game;
import gamesystem.services.api.GameService;
import gamesystem.services.api.OrderService;
import gamesystem.services.api.UserService;

public class ItemController {
    private OrderService orderService;
    private Cart cart;
    private GameService gameService;
    private AuthenticationContext authenticationContext;
    private UserService userService;

    public ItemController(OrderService orderService, GameService gameService, AuthenticationContext authenticationContext, UserService userService) {
        this.orderService = orderService;
        this.gameService = gameService;
        this.authenticationContext = authenticationContext;
        this.userService = userService;
    }

    public String add(String title){
            if(cart == null){
                this.cart = new Cart();
            }
           if(!this.gameService.checkIfGameExist(title)){
               return "Game doesn't exist!";
           }
           Game game = this.gameService.findGameByTitle(title);
           this.cart.addItem(game);
           return "Game added successfully";
    }

    public String remove(String title){
        if(cart == null){
            return "Game doesn't exist!";
        }

       Game game = this.gameService.findGameByTitle(title);
       this.cart.removeItem(game.getTitle());
       return title + " removed from cart.";

    }

    public String buy(){
        try{
            this.orderService.buyItems(this.cart.getItems(), this.authenticationContext.getLoggedInUser().getEmail());
            this.userService.buyItems(this.cart.getItems(), this.authenticationContext.getLoggedInUser().getEmail());

        }catch (Exception e){
            return e.getMessage();
        }
        return "Successfully bought!";
    }


}
