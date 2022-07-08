package web.dto;

import web.entities.Category;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
public class OrderDTO extends EntityDTO{
    private LocalDate localDateOrder;
    private String dateOrder;

    private LocalTime timeStartOrder;
    private String hourOrder;

    private LocalTime timeFinishOrder;
    private String hourFinishOrder;

    private LocalTime localDurationOrder;
    private String durationOrder;

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
