package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.entities.Category;
import web.entities.Order;
import web.entities.User;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AdminCompletedController implements Command {
    static final Logger userLogger = LogManager.getLogger(AdminCompletedController.class);


    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session =  req.getSession();
        User user = (User) session.getAttribute("user");

        try {
            Integer orderId = Integer.parseInt(req.getAttribute("orderId").toString());
            if (req.getParameter("method") != null && req.getParameter("method").equals("delete") && orderId != -1 ){
                OrderService.deleteOrderById(orderId);
                userLogger.info(String.format("Admin %d deleted %d order.", user.getId(), orderId));
            }
            if (req.getParameter("method") != null && req.getParameter("method").equals("update") && orderId != -1 && !req.getParameter("hourOrder").equals("--:--") ){

                Order order = OrderService.getOrderById(orderId);
                String completed = req.getParameter("completed");
                String paid = req.getParameter("paid");
                order.setCompleted(completed != null ? true : order.getCompleted() );
                order.setPaid(paid != null ? true : order.getPaid() );

                String date = req.getParameter("dateOrder");
                String time = req.getParameter("hourOrder");

                String[] d = date.split("-");
                String[] t = time.split(":");
                LocalDate localDate = LocalDate.of(Integer.parseInt(d[0]),Integer.parseInt(d[1]),Integer.parseInt(d[2]));
                LocalTime timeStartNewOrder = LocalTime.of(Integer.parseInt(t[0]),Integer.parseInt(t[1]));

                LocalTime timeFinishNewOrder = timeStartNewOrder;
                for (Category category : order.getCategories()) {
                    timeFinishNewOrder = timeFinishNewOrder.plusHours(category.getWorkTime().getHour());
                    timeFinishNewOrder = timeFinishNewOrder.plusMinutes(category.getWorkTime().getMinute());
                }

                List<Order> orderListMaster = OrderService.getOrdersByDateOrderAndMasterAndIdIsNot(localDate,order.getMasterId(),order.getId());
                boolean checkNewTime = true;
                if (orderListMaster.size() !=0 && timeFinishNewOrder.isAfter(orderListMaster.get(0).getTimeOrder())) {
                    for (int i = 0; i < orderListMaster.size() - 1; i++) {
                        LocalTime timeStartNextOrder = orderListMaster.get(i + 1).getTimeOrder();
                        LocalTime timeStartThisOrder = orderListMaster.get(i).getTimeOrder();
                        LocalTime timeFinishThisOrder = orderListMaster.get(i).getTimeOrder();
                        for (Category category : orderListMaster.get(i).getCategories()) {
                            timeFinishThisOrder = timeFinishThisOrder.plusHours(category.getWorkTime().getHour());
                            timeFinishThisOrder = timeFinishThisOrder.plusMinutes(category.getWorkTime().getMinute());
                        }
                        if (timeStartThisOrder.compareTo(timeStartNewOrder)==0 ||(timeStartThisOrder.isBefore(timeStartNewOrder)&&timeFinishThisOrder.isAfter(timeStartNewOrder)) || (timeStartNewOrder.isBefore(timeStartNextOrder) && timeFinishNewOrder.isAfter(timeStartNextOrder))) {
                            checkNewTime = false;
                            break;
                        }
                    }
                    LocalTime timeStartLastOrder = orderListMaster.get(orderListMaster.size()-1).getTimeOrder();
                    LocalTime timeFinishLastOrder = orderListMaster.get(orderListMaster.size()-1).getTimeOrder();
                    for (Category category : orderListMaster.get(orderListMaster.size()-1).getCategories()) {
                        timeFinishLastOrder = timeFinishLastOrder.plusHours(category.getWorkTime().getHour());
                        timeFinishLastOrder = timeFinishLastOrder.plusMinutes(category.getWorkTime().getMinute());
                    }
                    if (timeStartLastOrder.compareTo(timeStartNewOrder)==0 || (timeStartLastOrder.isBefore(timeStartNewOrder) && timeFinishLastOrder.isAfter(timeStartNewOrder)) || (timeStartNewOrder.isBefore(timeStartLastOrder) && timeFinishNewOrder.isAfter(timeStartLastOrder))) {
                        checkNewTime = false;
                    }
                }

                if (checkNewTime) {
                    order.setDateOrder(localDate);
                    order.setTimeOrder(timeStartNewOrder);
                }

                OrderService.updateOrder(order);
                userLogger.info(String.format("Admin %d update %d order.", user.getId(), orderId));
            }

            return new AdminCabinetController().execute(req,resp);

        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        } catch (NumberFormatException e) {
            userLogger.error("User tried enter wrong parametrs",e.fillInStackTrace());
            return "index.jsp";
        }
    }
}
