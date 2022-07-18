package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.dto.OrderDTO;
import web.entities.Category;
import web.entities.Master;
import web.entities.User;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.CategoryService;
import web.service.MasterService;
import web.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

public class AdminCabinetController implements Command {
    static final Logger userLogger = LogManager.getLogger(AdminCabinetController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session =  req.getSession();
        cleanSession(session);

        User user = (User) session.getAttribute("user");
        List<OrderDTO> orderDTOList = null;
        List<Master> masterList = null;

        try {
            masterList = MasterService.getAllMaster();
            session.setAttribute("masterList",masterList);

            Integer masterId = req.getParameter("masterSearch") != null
                    ? Integer.parseInt(req.getParameter("masterSearch")) :
                        session.getAttribute("masterSearch") != null ? (Integer) session.getAttribute("masterSearch") : -1;
            session.setAttribute("masterSearch",masterId);

            if (req.getParameter("dateVisitAdmin") != null && req.getParameter("dateVisitAdmin").equals("") ){
                 if (masterId == -1) {
                     orderDTOList = OrderService.getAllOrderDTOs();
                 } else {
                     orderDTOList = OrderService.getOrderDTOsMaster(masterId);
                 }
                session.removeAttribute("dateVisitAdmin");
            } else if ((req.getParameter("dateVisitAdmin") != null && !req.getParameter("dateVisitAdmin").equals("") ) || session.getAttribute("dateVisitAdmin") != null ) {
                String date = req.getParameter("dateVisitAdmin") != null && !req.getParameter("dateVisitAdmin").equals("") ? req.getParameter("dateVisitAdmin") : session.getAttribute("dateVisitAdmin").toString();
                session.setAttribute("dateVisitAdmin", date);
                String[] d = date.split("-");
                LocalDate localDate = LocalDate.of(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));
                if (masterId == -1) {
                    orderDTOList = OrderService.getOrderDTOsByDateOrde(localDate);
                } else {
                    orderDTOList = OrderService.getOrderDTOsByDateOrderAndMaster(localDate,masterId);
                }
            } else{
                if (masterId == -1) {
                    orderDTOList = OrderService.getAllOrderDTOs();
                } else {
                    orderDTOList = OrderService.getOrderDTOsMaster(masterId);
                }
            }



            // Start block pagination
            Integer pageSize = 6;
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
            userLogger.info(String.format("Admin (%d userId) received data", user.getId()));
            return "admin.jsp";
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        }
    }
}
