package web.entities;

import web.annotation.Column;
import web.annotation.TableDB;
import lombok.Data;

@TableDB(name = "role")
@Data
public class Role extends Entity{

    @Column(name = "name")
    private String name;
}
