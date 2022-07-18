package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.entities.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogOutController implements Command {

    static final Logger userLogger = LogManager.getLogger(LogOutController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("user");

        if(user!=null){
            userLogger.info(String.format("User %d logged out", user.getId()));
        }

        cleanSession(session);
        session.removeAttribute("user");
        session.removeAttribute("clientUser");
        session.removeAttribute("masterUser");
        session.removeAttribute("dateVisitMaster");
        session.removeAttribute("dateVisitAdmin");
        session.removeAttribute("masterSearch");

        return "index.jsp";
    }
}
