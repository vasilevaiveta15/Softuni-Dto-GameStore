package gamesystem.models;


import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Game {
    private Long id;
    private String title;
    private BigDecimal price;
    private BigDecimal size;
    private String trailer;
    private String thumbnailUrl;
    private String description;
    private Set<Order> orders;


    public Game() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Size(min=3, max = 100)
    @Pattern(regexp = "([A-Z].*)")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Digits(integer = 5, fraction = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if(price.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Price must be a positive number!");
        }
        this.price = price;
    }

    @Digits(integer = 5, fraction = 1)
    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        if(size.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Size must be a positive number!");
        }
        this.size = size;
    }

    @Size(min = 11, max = 11)
    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    //@Pattern(regexp = "^(ttp:\\/\\/|https:\\/\\/)+[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$")
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Size(min=20)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", size=" + size +
                ", trailer='" + trailer + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", description='" + description + '\'' +
                ", orders=" + orders +
                '}';
    }
}
