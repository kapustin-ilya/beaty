package web.filter;

import web.entities.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/b")
public class RoleCheckFilter extends HttpFilter {
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        Boolean check = true;
        User user = (User) req.getSession().getAttribute("user");

        switch (req.getParameter("command")) {
            case "login": check = (user==null ? true : false); break;
            case "registration": check = (user==null || user.getRoleId() == 3? true : false); break;
            case "userCabinet": check = (user!=null && user.getRoleId() == 1 ? true : false); break;
            case "recordingCabinet": check = (user!=null && user.getRoleId() == 1 ? true : false); break;
            case "newRecording": check = (user!=null && user.getRoleId() == 1 ? true : false); break;
            case "masterCabinet": check = (user!=null && user.getRoleId() == 3 ? true : false); break;
            case "masterCompleted": check = (user!=null && user.getRoleId() == 3 ? true : false); break;
            case "userComment": check = (user!=null && user.getRoleId() == 1 ? true : false); break;
            case "adminCabinet": check = (user!=null && user.getRoleId() == 2 ? true : false); break;
            case "adminCompleted": check = (user!=null && user.getRoleId() == 2 ? true : false); break;
            case "profileCabinet": check = (user!=null ? true : false); break;
            case "profileUpdate": check = (user!=null ? true : false); break;
        }

        if (!check) {
            resp.sendRedirect("index.jsp");
            return;
        }
        chain.doFilter(req, resp);
    }
}

