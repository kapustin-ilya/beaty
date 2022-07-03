package web.entities;

import web.annotation.Column;
import web.annotation.ManyToOne;
import web.annotation.TableDB;
import lombok.Data;

@Data
@TableDB(name = "comment")
public class Comment extends Entity{
    @Column(name="detail")
    private String detail;

    @Column(name="order_id")
    private Integer orderId;
    @ManyToOne(foreignTable ="order", foreignColumnDB ="order_id")
    private Order order; //order_id
}
