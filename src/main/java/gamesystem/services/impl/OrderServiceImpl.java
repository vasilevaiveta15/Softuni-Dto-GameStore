package gamesystem.services.impl;

import gamesystem.models.Game;
import gamesystem.models.Order;
import gamesystem.repositories.OrderRepository;
import gamesystem.services.api.OrderService;
import gamesystem.services.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @Override
    public void buyItems(Collection<Game> product, String email) {
        Order order = new Order();
        order.setProducts(product);
        order.setBuyer(this.userService.getUserByEmail(email));
        this.orderRepository.save(order);
    }
}
