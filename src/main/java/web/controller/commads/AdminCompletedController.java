package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
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
            if (req.getParameter("method") != null && req.getParameter("method").equals("update") && orderId != -1){

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
                LocalTime localTime = LocalTime.of(Integer.parseInt(t[0]),Integer.parseInt(t[1]));
                LocalDateTime localDateTime = LocalDateTime.of(localDate,localTime);
                order.setTimeOrder(localDateTime);

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
