package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.dto.OrderDTO;
import web.entities.Category;
import web.entities.Client;
import web.entities.Order;
import web.entities.User;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.CategoryService;
import web.service.ClientService;
import web.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class NewRecordingController implements Command {
    static final Logger userLogger = LogManager.getLogger(NewRecordingController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("user");
        try {
            Client client = (Client) session.getAttribute("clientUser");
            Integer idClient = client.getId();
            String idMaster = req.getParameter("master");


            String date = req.getParameter("dateOrder");
            String time = req.getParameter("timeOrder");
            String[] d = date.split("-");
            String[] t = time.split(":");
            LocalDate localDate = LocalDate.of(Integer.parseInt(d[0]),Integer.parseInt(d[1]),Integer.parseInt(d[2]));
            LocalTime timeStartNewOrder = LocalTime.of(Integer.parseInt(t[0]),Integer.parseInt(t[1]));


            LocalTime timeFinishNewOrder = timeStartNewOrder;
            Set<Category> categorySet = new TreeSet<>((c1, c2)->c1.getId().compareTo(c2.getId()));
            String idCategoryFirst = req.getParameter("categoryFirst");
            Category categoryFirst = new Category();
            if (!idCategoryFirst.equals("-1")) {
                categoryFirst = CategoryService.getCategoryById(Integer.parseInt(idCategoryFirst));
                categorySet.add(categoryFirst);
                timeFinishNewOrder = timeFinishNewOrder.plusHours(categoryFirst.getWorkTime().getHour());
                timeFinishNewOrder = timeFinishNewOrder.plusMinutes(categoryFirst.getWorkTime().getMinute());
            }
            String idCategorySecond = req.getParameter("categorySecond");
            Category categorySecond = new Category();
            if (!idCategorySecond.equals("-1")) {
                categorySecond = CategoryService.getCategoryById(Integer.parseInt(idCategorySecond));
                categorySet.add(categorySecond);
                timeFinishNewOrder = timeFinishNewOrder.plusHours(categorySecond.getWorkTime().getHour());
                timeFinishNewOrder = timeFinishNewOrder.plusMinutes(categorySecond.getWorkTime().getMinute());
            }
            String idCategoryThird = req.getParameter("categoryThird");
            Category categoryThird = new Category();
            if (!idCategoryThird.equals("-1")) {
                categoryThird = CategoryService.getCategoryById(Integer.parseInt(idCategoryThird));
                categorySet.add(categoryThird);
                timeFinishNewOrder = timeFinishNewOrder.plusHours(categoryThird.getWorkTime().getHour());
                timeFinishNewOrder = timeFinishNewOrder.plusMinutes(categoryThird.getWorkTime().getMinute());
            }

            List<Order> orderListMaster = OrderService.getOrdersByDateOrderAndMaster(localDate,Integer.parseInt(idMaster));

            if (orderListMaster.size() !=0 && timeFinishNewOrder.isAfter(orderListMaster.get(0).getTimeOrder())) {
                for (int i = 0; i < orderListMaster.size() - 1; i++) {
                    LocalTime timeStartNextOrder = orderListMaster.get(i + 1).getTimeOrder();
                    LocalTime timeStartThisOrder = orderListMaster.get(i).getTimeOrder();
                    LocalTime timeFinishThisOrder = orderListMaster.get(i).getTimeOrder();
                    for (Category category : orderListMaster.get(i).getCategories()) {
                        timeFinishThisOrder = timeFinishThisOrder.plusHours(category.getWorkTime().getHour());
                        timeFinishThisOrder = timeFinishThisOrder.plusMinutes(category.getWorkTime().getMinute());
                    }
                    if (timeStartThisOrder.compareTo(timeStartNewOrder)==0 || (timeStartThisOrder.isBefore(timeStartNewOrder)&&timeFinishThisOrder.isAfter(timeStartNewOrder)) || (timeStartNewOrder.isBefore(timeStartNextOrder) && timeFinishNewOrder.isBefore(timeStartNextOrder))) {
                        return new RecordingCabinetController().execute(req, resp);
                    }
                }
                LocalTime timeStartLastOrder = orderListMaster.get(orderListMaster.size()-1).getTimeOrder();
                LocalTime timeFinishLastOrder = orderListMaster.get(orderListMaster.size()-1).getTimeOrder();
                for (Category category : orderListMaster.get(orderListMaster.size()-1).getCategories()) {
                    timeFinishLastOrder = timeFinishLastOrder.plusHours(category.getWorkTime().getHour());
                    timeFinishLastOrder = timeFinishLastOrder.plusMinutes(category.getWorkTime().getMinute());
                }
                if (timeStartLastOrder.compareTo(timeStartNewOrder)==0 || (timeStartLastOrder.isBefore(timeStartNewOrder) && timeFinishLastOrder.isAfter(timeStartNewOrder))) {
                    return new RecordingCabinetController().execute(req, resp);
                }
            }




            Order newOrder = new Order();
            newOrder.setCategories(categorySet);
            newOrder.setClientId(idClient);
            newOrder.setMasterId(Integer.parseInt(idMaster));
            newOrder.setDateOrder(localDate);
            newOrder.setTimeOrder(timeStartNewOrder);

            newOrder = OrderService.addNewOrderInDB(newOrder);

            userLogger.info(String.format("User (%d userId) made new recording (%d orderId).", user.getId(), newOrder.getId()));

            return new UserCabinetController().execute(req,resp);
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        } catch (NumberFormatException e) {
            userLogger.error("User tried enter wrong parametrs",e.fillInStackTrace());
            return "index.jsp";
        }
    }
}
