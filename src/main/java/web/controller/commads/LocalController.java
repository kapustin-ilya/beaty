package web.controller.commads;

import web.controller.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LocalController implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession session = req.getSession();
        cleanSession(session);

        String lang = req.getAttribute("lang").toString();
        session.setAttribute("lang", lang);

        return "index.jsp";
    }
}
