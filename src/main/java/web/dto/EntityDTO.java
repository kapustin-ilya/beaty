package web.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class EntityDTO implements Serializable, Cloneable {
    public Integer id;
}