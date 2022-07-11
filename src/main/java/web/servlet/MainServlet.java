package web.servlet;

import web.controller.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@MultipartConfig
@WebServlet (name ="MainServlet" , urlPatterns="/b" )
public class MainServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        String nameCommand = req.getParameter("command") != null ?  req.getParameter("command") : "";
        Command command = MapCommand.getCommand(nameCommand);
        String view = command != null ? command.execute(req,resp) : "index.jsp";
        resp.sendRedirect(view);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        String nameCommand = req.getParameter("command") != null ?  req.getParameter("command") : "";
        Command command = MapCommand.getCommand(nameCommand);
        String view = command != null ? command.execute(req,resp) : "index.jsp";
        resp.sendRedirect(view);
    }
}
