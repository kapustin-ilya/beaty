package web.entities;

import web.annotation.*;
import lombok.Data;

import java.util.Set;

@Data
@TableDB(name = "master")
public class Master extends Entity{
    @Column(name = "level_quality")
    private String levelQuality; //enum('low', 'hight')
    @Column(name = "rating_sum")
    private Integer ratingSum;
    @Column(name = "rating_count")
    private Integer ratingCount;
    @Column(name = "adress_photo")
    private String adressPhoto;

    @Column(name = "user_id")
    private Integer userId;
    @ManyToOne(foreignTable = "user", foreignColumnDB = "user_id")
    private User user; // user_id

    @ManyToMany(foreignTable = "category", tableForManyToMany = "master_has_category", mainColumnDB = "master_id", foreignColumnDB = "category_id")
    private Set<Category> categories; // table master_has_category

    @OneToMany(foreignTable = "order", mainColumnDB = "master_id")
    private Set<Order> orders; // This isn`t in database
}
