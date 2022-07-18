package web.entities;


import web.annotation.Column;
import web.annotation.TableDB;
import lombok.Data;

import java.io.Serializable;


@Data
public class Entity implements Serializable, Cloneable {
    @Column(name = "id")
    Integer id;
}
