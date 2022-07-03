package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.dto.OrderDTO;
import web.entities.Client;
import web.entities.User;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.ClientService;
import web.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class UserCabinetController implements Command {

    static final Logger userLogger = LogManager.getLogger(UserCabinetController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        cleanSession(session);

        User user = (User) session.getAttribute("user");
        try{
            Client client = ClientService.getAllClients().stream().filter(c->c.getUserID().equals(user.getId())).findFirst().get();
            Integer idClient = client.getId();
            List<OrderDTO> orderDTOList = OrderService.getOrderDTOsClient(idClient);

            // Start block pagination
            Integer pageSize = 3;
            Integer orderCount = orderDTOList.size();
            if(req.getParameter("pageSize") != null) {
                pageSize=Integer.parseInt(req.getParameter("pageSize"));
            }

            session.setAttribute("numberPages", (orderCount/pageSize + (orderCount%pageSize != 0 ? 1 : 0)));

            Integer page = 1;
            if (req.getParameter("page") != null ) {
                page = Integer.parseInt(req.getParameter("page"));
            }

            session.setAttribute("page",page);

            // Finish block pagination

            session.setAttribute("orderDTOList",orderDTOList.subList((page-1)*pageSize,
                    ((page-1)*pageSize+pageSize)<orderDTOList.size()?((page-1)*pageSize+pageSize):orderDTOList.size()));


            session.setAttribute("userCabinet",true);
            return "index.jsp";
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        } catch (NumberFormatException e) {
            userLogger.error("User tried enter wrong parametrs",e.fillInStackTrace());
            return "index.jsp";
        }
    }
}
