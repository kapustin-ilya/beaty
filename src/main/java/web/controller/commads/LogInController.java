package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.custom.HashPassword;
import web.entities.Client;
import web.entities.Master;
import web.entities.User;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.ClientService;
import web.service.MasterService;
import web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class LogInController implements Command {
    static final Logger userLogger = LogManager.getLogger(LogInController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession session = req.getSession();
        cleanSession(session);

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            List<User> user = UserService.getUsersByEmailAndPassword(email, HashPassword.run(password));
            if (user.size() != 0){
                session.setAttribute("user",user.get(0));
                userLogger.info(String.format("User %d logged in", user.get(0).getId()));
                if (user.get(0).getRoleId() == 1){
                    Client client = ClientService.getClientByUserId(user.get(0).getId());
                    session.setAttribute("clientUser",client);
                }
                if (user.get(0).getRoleId() == 3){
                    Master master = MasterService.getMasterByUserId(user.get(0).getId());
                    session.setAttribute("masterUser",master);
                }
            } else {
                session.setAttribute("errorEnter", "You`s entered wrong email or password");
            }

            return "index.jsp";
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        }
    }
}
