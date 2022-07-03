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

public class MasterCompletedController implements Command {
    static final Logger userLogger = LogManager.getLogger(MasterCompletedController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        try{
            String completed = req.getParameter("completed").toString();
            Integer orderId = Integer.parseInt(req.getParameter("orderId"));
            if (orderId != -1){
                Order order = OrderService.getOrderById(orderId);
                order.setCompleted(completed.equals("on")?  true : false);
                OrderService.updateOrder(order);
                userLogger.info(String.format("Admin (%d userId) update %d order.", user.getId(), orderId));
            }
            return new MasterCabinetController().execute(req,resp);
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        } catch (NumberFormatException e) {
            userLogger.error("User tried enter wrong parametrs",e.fillInStackTrace());
            return "index.jsp";
        }
    }
}
