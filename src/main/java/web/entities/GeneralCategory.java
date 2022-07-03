package web.entities;

import web.annotation.Column;
import web.annotation.TableDB;
import web.annotation.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
@TableDB(name = "general_category")
public class GeneralCategory extends Entity{
    @Column(name = "name")
    private String name;

    @OneToMany(foreignTable = "category", mainColumnDB = "general_category_id")
    private Set<Category> categories; // This isn`t in Database
}
