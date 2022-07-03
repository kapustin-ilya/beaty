package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.dto.OrderDTO;
import web.entities.Master;
import web.entities.User;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.MasterService;
import web.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class MasterCabinetController implements Command {
    static final Logger userLogger = LogManager.getLogger(MasterCabinetController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        cleanSession(session);

        try {
            User user = (User) session.getAttribute("user");
            Master master = MasterService.getAllMaster().stream().filter(m->m.getUserId().equals(user.getId())).findFirst().get();
            Integer idMaster = master.getId();


            List<OrderDTO> orderDTOList = OrderService.getOrderDTOsMaster(idMaster);

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

            session.setAttribute("masterCabinet",true);
            userLogger.info(String.format("Master (%d userId) received data", user.getId()));
            return "index.jsp";
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        }
    }
}
