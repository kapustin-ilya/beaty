package web.entities;

import web.annotation.Column;
import web.annotation.ManyToMany;
import web.annotation.ManyToOne;
import web.annotation.TableDB;
import lombok.Data;

import java.sql.Time;
import java.util.Set;

@Data
@TableDB(name = "category")
public class Category extends Entity{
    @Column(name = "name")
    private String name;
    @Column(name = "work_time")
    private Time workTime;
    @Column(name = "price_low")
    private Double priceLow;
    @Column(name = "price_hight")
    private Double priceHight;

    @Column(name = "general_category_id")
    private Integer generalCategoryId;

    @ManyToOne (foreignTable = "general_category", foreignColumnDB = "general_category_id")
    private GeneralCategory generalCategory; //general_category_id

    @ManyToMany(foreignTable ="master", tableForManyToMany = "master_has_category", mainColumnDB = "category_id", foreignColumnDB = "master_id")
    private Set<Master> masters; //table master_has_category
}
