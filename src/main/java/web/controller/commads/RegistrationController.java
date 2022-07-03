package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
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

public class RegistrationController implements Command {
    static final Logger userLogger = LogManager.getLogger(RegistrationController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        cleanSession(session);

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String surname = req.getParameter("surname");
        String name = req.getParameter("name");
        String phoneNumber = req.getParameter("phoneNumber");
        try {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setName(name);
            newUser.setSername(surname);
            newUser.setPhoneNumber(phoneNumber);
            if (req.getParameter("role") == null) {
                newUser.setRoleId(1);
                newUser = UserService.addNewUserInDB(newUser);
                if (newUser.getId() != null) {
                    Client newClient = new Client();
                    newClient.setUserID(newUser.getId());
                    newClient = ClientService.addNewClient(newClient);
                    session.setAttribute("user", newUser);
                    session.setAttribute("clientUser", newClient);
                    userLogger.info(String.format("Created new user (%d userId).", newUser.getId()));
                } else {
                    session.setAttribute("errorRegistration", "This email is pass");
                }
            } else if (req.getParameter("role").equals("2")){
                newUser.setRoleId(2);
                newUser = UserService.addNewUserInDB(newUser);
                if (newUser.getId() == null) {
                    session.setAttribute("errorRegistration", "This email is pass");
                } else {
                    userLogger.info(String.format("Created new user (%d userId).", newUser.getId()));
                    session.setAttribute("errorRegistration", "Created new admin");
                }
            } else if (req.getParameter("role").equals("3")){
                newUser.setRoleId(3);
                newUser = UserService.addNewUserInDB(newUser);
                if (newUser.getId() == null) {
                    session.setAttribute("errorRegistration", "This email is pass");
                } else {
                    Master newMaster = new Master();
                    newMaster.setUserId(newUser.getId());
                    newMaster = MasterService.addNewMaster(newMaster);
                    userLogger.info(String.format("Created new user (%d userId).", newUser.getId()));
                    session.setAttribute("errorRegistration", "Created new master");
                }
            }

            return "index.jsp";
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        }
    }
}
