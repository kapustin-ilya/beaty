package web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@WebFilter(urlPatterns = "/b")
public class ValidationDataFilter extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        Boolean check = false;
        switch (req.getParameter("command")) {
            case "login": check = checkFormLogIn (req,resp); break;
            case "category": checkCategory (req,resp); check = true; break;
            case "adminCompleted": checkAdminCompleted (req,resp); check = true; break;
            case "local": checkLocal (req,resp); check = true; break;
            case "masterCompleted": checkMasterCompleted (req,resp); check = true; break;
            case "newRecording": check = checkNewRecording (req,resp); break;
            case "profileUpdate": check = checkProfileUpdate (req,resp); break;
            case "registration": check = checkRegistration (req,resp); break;
            default: check = true;
        }
        if (!check) {
            resp.sendRedirect("index.jsp");
            return;
        }
        chain.doFilter(req, resp);
    }

    private Boolean checkRegistration(HttpServletRequest req, HttpServletResponse resp) {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        return Pattern.matches("[a-zA-Z0-9]+[a-zA-Z0-9.]*\\@\\w+\\.\\w+",email) ? (password.length()>=5 ? true : false ): false;
    }

    private Boolean checkProfileUpdate(HttpServletRequest req, HttpServletResponse resp) {
        String password = req.getParameter("password");
        return password.length()>=5 ? true : false;
    }

    private Boolean checkNewRecording(HttpServletRequest req, HttpServletResponse resp) {
        String idMaster = req.getParameter("master");
        String idCategoryFirst = req.getParameter("categoryFirst");
        String idCategorySecond = req.getParameter("categorySecond");
        String idCategoryThird = req.getParameter("categoryThird");
        String time = req.getParameter("timeOrder");
        String date = req.getParameter("dateOrder");
        return (!idMaster.equals("-1")&&(!idCategoryFirst.equals("-1")||!idCategorySecond.equals("-1")||!idCategoryThird.equals("-1"))
                    && time != null && !date.equals("--:--")) ? true : false;
    }

    private void checkMasterCompleted(HttpServletRequest req, HttpServletResponse resp) {
        String orderId = req.getParameter("orderId");
        req.setAttribute("orderId",((orderId != null && Pattern.matches("^([1-9][0-9]*)|0$",orderId)) ? orderId : "-1"));
    }

    private void checkLocal(HttpServletRequest req, HttpServletResponse resp) {
        String lang = req.getParameter("lang");
        req.setAttribute("lang",((lang != null && (lang.equals("en") || lang.equals("uk"))) ? lang : "en"));
    }

    private void checkAdminCompleted(HttpServletRequest req, HttpServletResponse resp) {
        String orderId = req.getParameter("orderId");
        req.setAttribute("orderId",((orderId != null && Pattern.matches("^([1-9][0-9]*)|0$",orderId)) ? orderId : "-1"));

    }

    private boolean checkFormLogIn(HttpServletRequest req, HttpServletResponse resp){
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        return Pattern.matches("[a-zA-Z0-9]+[a-zA-Z0-9.]*\\@\\w+\\.\\w+",email) ? (password.length()>=5 ? true : false ): false;
    }
    private void checkCategory(HttpServletRequest req, HttpServletResponse resp){
        String generalCategory = req.getParameter("generalCategory");
        String category = req.getParameter("category");
        String name = req.getParameter("name");
        String rating = req.getParameter("rating");
        String pageSize = req.getParameter("pageSize");
        String page = req.getParameter("page");

        req.setAttribute("generalCategory",((generalCategory != null && Pattern.matches("^((\\+|-)?[1-9][0-9]*)|0$",generalCategory)) ? generalCategory: "-1"));
        req.setAttribute("category",((category != null && Pattern.matches("^([1-9][0-9]*)|0$",category)) ? category : null));
        req.setAttribute("name",((name != null && (name.equals("ASC") || name.equals("DESC"))) ? name : null));
        req.setAttribute("rating",((rating != null && (rating.equals("ASC") || rating.equals("DESC"))) ? rating : null));
        req.setAttribute("pageSize",((pageSize != null && Pattern.matches("^([1-9][0-9]*)|0$",pageSize)) ? pageSize : null));
        req.setAttribute("page",((page != null && Pattern.matches("^([1-9][0-9]*)|0$",page)) ? page : null));
    }

}
