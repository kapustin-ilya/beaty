package web.entities;

import web.annotation.Column;
import web.annotation.ManyToOne;
import web.annotation.TableDB;
import web.annotation.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
@TableDB(name = "client")
public class Client extends Entity{
    @Column(name = "order_sum")
    private Integer orderSum;

    @Column(name = "user_id")
    private Integer userID;
    @ManyToOne(foreignTable = "user", foreignColumnDB = "user_id")
    private User user; // user_id

    @OneToMany(foreignTable = "order", mainColumnDB = "client_id")
    private Set<Order> orders; //This isn`t in DataBase
}
