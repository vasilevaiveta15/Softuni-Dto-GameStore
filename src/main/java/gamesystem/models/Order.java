package gamesystem.models;


import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "orders")
public class Order{
    private Long id;
    private User buyer;
    private Collection<Game> products;

    public Order() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    @ManyToMany()
    @JoinTable(name = "orders_products", joinColumns = @JoinColumn(name = "order_id"),
    inverseJoinColumns = @JoinColumn(name = "game_id"))
    public Collection<Game> getProducts() {
        return products;
    }

    public void setProducts(Collection<Game> products) {
        this.products = products;
    }
}
