package web.dto;

import web.entities.Category;
import lombok.Data;

import java.util.Set;

@Data
public class OrderDTO extends EntityDTO{
    private String dateOrder;
    private String hourOrder;
    private Boolean completed;
    private Boolean paid;

    private Integer masterId;
    private String masterName;

    private Integer clientId;
    private String clientName;

    private Set<Category> categories;
    private Double sumOrder;

    private String comment;
}
