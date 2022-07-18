package web.service;

import web.dto.OrderDTO;
import web.entities.Category;
import web.entities.Comment;
import web.entities.Order;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.DBManager;
import web.repository.impl.OrderDAOImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderService {
    static final String SQL_FIND_Order_BY_CLIENT_ID = "select * from orders where client_id = ? order by date_order desc, time_order desc;";
    static final String SQL_FIND_Order_BY_MASTER_ID = "select * from orders where master_id = ? order by date_order desc, time_order desc;";
    static final String SQL_FIND_ALL_Order = "select * from orders order by date_order desc, time_order desc;";

    static final String SQL_FIND_Order_BY_DATE_ORDER_AND_MASTER_ID = "select * from orders where date_order = ? and master_id=? order by time_order asc;";
    static final String SQL_FIND_Order_BY_DATE_ORDER = "select * from orders where date_order = ? order by time_order asc;";
    static final String SQL_FIND_Order_BY_DATE_ORDER_AND_MASTER_ID_AND_ID_IS_NOT = "select * from orders where date_order = ? and master_id=? and id!=? order by time_order asc;";


    public static Order getOrderById(Integer id) throws DBException, EntityException {
        return new OrderDAOImpl().findElementById(DBManager.getInstance().getConnection(),id,true);
    }
    public static Order addNewOrderInDB (Order order) throws DBException, EntityException{
        return new OrderDAOImpl().insert(DBManager.getInstance().getConnection(),order);
    }
    public static List<OrderDTO> getOrderDTOsClient(Integer clientId) throws DBException, EntityException{
        List<Order> orderList = new OrderDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_Order_BY_CLIENT_ID,true, clientId);
        return getOrderDTOs(orderList);
    }
    public static List<OrderDTO> getOrderDTOsMaster(Integer masterId) throws DBException, EntityException{
        List<Order> orderList = new OrderDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_Order_BY_MASTER_ID,true, masterId);
        return getOrderDTOs(orderList);
    }
    public static List<Order> getOrdersByDateOrderAndMaster(LocalDate localDate, Integer masterId) throws DBException, EntityException{
        return new OrderDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_Order_BY_DATE_ORDER_AND_MASTER_ID,true, localDate,masterId);
    }
    public static List<OrderDTO> getOrderDTOsByDateOrderAndMaster(LocalDate localDate, Integer masterId) throws DBException, EntityException{
        List<Order> orderList = new OrderDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_Order_BY_DATE_ORDER_AND_MASTER_ID,true, localDate,masterId);
        return getOrderDTOs(orderList);
    }
    public static List<OrderDTO> getOrderDTOsByDateOrde(LocalDate localDate) throws DBException, EntityException{
        List<Order> orderList = new OrderDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_Order_BY_DATE_ORDER,true, localDate);
        return getOrderDTOs(orderList);
    }
    public static List<Order> getOrdersByDateOrderAndMasterAndIdIsNot(LocalDate localDate, Integer masterId, Integer orderID) throws DBException, EntityException{
        return new OrderDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_Order_BY_DATE_ORDER_AND_MASTER_ID_AND_ID_IS_NOT,true, localDate,masterId,orderID);
    }
    public static Order updateOrder (Order order) throws DBException, EntityException{
        return new OrderDAOImpl().update(DBManager.getInstance().getConnection(),order);
    }

    public static List<Order> getAllOrders() throws DBException, EntityException{
        return new OrderDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_ALL_Order,true);
    }

    public static boolean deleteOrderById(Integer id) throws DBException, EntityException{
        return new OrderDAOImpl().deleteById(DBManager.getInstance().getConnection(),id);
    }
    public static List<OrderDTO> getAllOrderDTOs() throws DBException, EntityException{
        List<Order> orderList = getAllOrders();
        return getOrderDTOs(orderList);
    }


    private static List<OrderDTO> getOrderDTOs(List<Order> orderList) throws DBException, EntityException{
        List<OrderDTO> orderDTOList = new ArrayList<>();
        List<Comment> commentList = CommentService.getComments();
        for (Order order : orderList){
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());

            orderDTO.setLocalDateOrder(order.getDateOrder());
            orderDTO.setTimeStartOrder(order.getTimeOrder());
            LocalTime timeFinishOrder = order.getTimeOrder();

            orderDTO.setDateOrder(order.getDateOrder().toString());
            orderDTO.setHourOrder(order.getTimeOrder().toString());

            orderDTO.setCompleted(order.getCompleted());
            orderDTO.setPaid(order.getPaid());

            orderDTO.setMasterId(order.getMasterId());
            orderDTO.setMasterName(UserService.getUserById(MasterService.getMasterById(order.getMasterId()).getUserId()).getName());

            orderDTO.setClientId(order.getClientId());
            orderDTO.setClientName(UserService.getUserById(ClientService.getClientById(order.getClientId()).getUserID()).getName());

            Optional<Comment> commentOptional = commentList.stream().filter(c->c.getOrderId().equals(order.getId())).findFirst();

            if(commentOptional.isPresent()) {
                orderDTO.setComment(commentOptional.get().getDetail());
            } else {
                orderDTO.setComment("");
            }

            orderDTO.setCategories(order.getCategories());
            Double sum = 0.0;
            LocalTime durationOrder = LocalTime.of(0,0);
            for (Category category : order.getCategories()){
                LocalTime workTimeCategory = category.getWorkTime();
                timeFinishOrder = timeFinishOrder.plusHours(workTimeCategory.getHour());
                timeFinishOrder = timeFinishOrder.plusMinutes(workTimeCategory.getMinute());
                durationOrder = durationOrder.plusHours(workTimeCategory.getHour());
                durationOrder = durationOrder.plusMinutes(workTimeCategory.getMinute());


                if (order.getMaster().getLevelQuality().equals("low")){
                    sum += category.getPriceLow();
                } else {
                    sum += category.getPriceHight();
                }
            }
            orderDTO.setSumOrder(sum);

            orderDTO.setTimeFinishOrder(timeFinishOrder);
            orderDTO.setHourFinishOrder(timeFinishOrder.toString());

            orderDTO.setLocalDurationOrder(durationOrder);
            orderDTO.setDurationOrder(durationOrder.toString());

            orderDTOList.add(orderDTO);

        }
               
        return orderDTOList;
    }
}
