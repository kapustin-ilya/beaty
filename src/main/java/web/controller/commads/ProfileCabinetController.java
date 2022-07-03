package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.entities.Category;
import web.entities.User;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ProfileCabinetController implements Command {
    static final Logger userLogger = LogManager.getLogger(ProfileCabinetController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        cleanSession(session);

        User user = (User) session.getAttribute("user");

        try {
            List<Category> categoryList = CategoryService.getAllCategories();
            session.setAttribute("categoryList",categoryList);
            session.setAttribute("profileCabinet",true);
            return "index.jsp";
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        }
    }
}
