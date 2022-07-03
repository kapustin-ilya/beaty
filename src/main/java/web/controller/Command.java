package web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface Command {
    String execute(HttpServletRequest req, HttpServletResponse resp);

    default void cleanSession(HttpSession session){
        session.removeAttribute("errorRegistration");
        session.removeAttribute("errorEnter");
        session.removeAttribute("userCabinet");
        session.removeAttribute("recordingCabinet");
        session.removeAttribute("masterCabinet");
        session.removeAttribute("profileCabinet");
    }
}
