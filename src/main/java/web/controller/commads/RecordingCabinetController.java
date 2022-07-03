package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.entities.Category;
import web.entities.GeneralCategory;
import web.entities.Master;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.CategoryService;
import web.service.MasterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class RecordingCabinetController implements Command {
    static final Logger userLogger = LogManager.getLogger(RecordingCabinetController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        cleanSession(session);

        try {
            List<GeneralCategory> generalCategoryList = CategoryService.getAllGeneralCategories();
            List<Master> masterList = MasterService.getAllMaster();
            List<Category> categoryList = CategoryService.getAllCategories();
            session.setAttribute("masterList",masterList);
            session.setAttribute("generalCategoryList",generalCategoryList);
            session.setAttribute("categoryList",categoryList);
            session.setAttribute("recordingCabinet",true);
            return "index.jsp";
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        }
    }
}
