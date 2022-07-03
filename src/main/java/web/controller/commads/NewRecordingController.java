package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.entities.Category;
import web.entities.Client;
import web.entities.Order;
import web.entities.User;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.ClientService;
import web.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.TreeSet;

public class NewRecordingController implements Command {
    static final Logger userLogger = LogManager.getLogger(NewRecordingController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("user");
        try {
            Client client = ClientService.getAllClients().stream().filter(c->c.getUserID().equals(user.getId())).findFirst().get();
            String idMaster = req.getParameter("master");
            Integer idClient = client.getId();
            Set<Category> categorySet = new TreeSet<>((c1, c2)->c1.getId().compareTo(c2.getId()));
            String idCategoryFirst = req.getParameter("categoryFirst");
            Category categoryFirst = new Category();
            if (!idCategoryFirst.equals("-1")) {
                categoryFirst.setId(Integer.parseInt(idCategoryFirst));
                categorySet.add(categoryFirst);
            }
            String idCategorySecond = req.getParameter("categorySecond");
            Category categorySecond = new Category();
            if (!idCategorySecond.equals("-1")) {
                categorySecond.setId(Integer.parseInt(idCategorySecond));
                categorySet.add(categorySecond);
            }
            String idCategoryThird = req.getParameter("categoryThird");
            Category categoryThird = new Category();
            if (!idCategoryThird.equals("-1")) {
                categoryThird.setId(Integer.parseInt(idCategoryThird));
                categorySet.add(categoryThird);
            }
            String date = req.getParameter("dateOrder");
            String time = req.getParameter("timeOrder");

            String[] d = date.split("-");
            String[] t = time.split(":");
            LocalDate localDate = LocalDate.of(Integer.parseInt(d[0]),Integer.parseInt(d[1]),Integer.parseInt(d[2]));
            LocalTime localTime = LocalTime.of(Integer.parseInt(t[0]),Integer.parseInt(t[1]));
            LocalDateTime localDateTime = LocalDateTime.of(localDate,localTime);

            Order newOrder = new Order();
            newOrder.setCategories(categorySet);
            newOrder.setClientId(idClient);
            newOrder.setMasterId(Integer.parseInt(idMaster));
            newOrder.setTimeOrder(localDateTime);

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
