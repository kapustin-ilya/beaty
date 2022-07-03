package web.entities;

import web.annotation.Column;
import web.annotation.ManyToMany;
import web.annotation.ManyToOne;
import web.annotation.TableDB;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@TableDB(name = "orders")
@Data
public class Order extends Entity{
    @Column(name = "time_order")
    private LocalDateTime timeOrder;
    @Column(name = "completed")
    private Boolean completed;
    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "master_id")
    private Integer masterId;
    @Column(name = "client_id")
    private Integer clientId;

    @ManyToOne(foreignTable ="master", foreignColumnDB = "master_id")
    private Master master; //master_id
    @ManyToOne(foreignTable ="client", foreignColumnDB = "client_id")
    private Client client; //client_id

    @ManyToMany(foreignTable = "category", tableForManyToMany = "order_has_category", mainColumnDB ="order_id", foreignColumnDB = "category_id" )
    private Set<Category> categories; // table order_has_category
}
