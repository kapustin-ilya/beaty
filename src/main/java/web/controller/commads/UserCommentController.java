package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.entities.Comment;
import web.entities.Master;
import web.entities.User;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.CommentService;
import web.service.MasterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class UserCommentController implements Command {
    static final Logger userLogger = LogManager.getLogger(UserCommentController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        try {
            Integer orderId = Integer.parseInt(req.getParameter("orderId"));
            Integer masterId = Integer.parseInt(req.getParameter("masterId"));
            Master master = MasterService.getMasterById(masterId);
            if (!req.getParameter("rating").equals("") && req.getParameter("rating") != null){
                String rating = req.getParameter("rating");
                master.setRatingSum((master.getRatingSum()+Integer.parseInt(rating)));
                master.setRatingCount(master.getRatingCount()+1);
                userLogger.info(String.format("User %d left a rating %d order.", user.getId(), orderId));
                master = MasterService.updateMaster(master);
            }

            if (!req.getParameter("comment").equals("") && req.getParameter("rating") != null) {
                String detail = req.getParameter("comment");
                List<Comment> commentList = CommentService.getCommentsByOrderId(orderId);
                for (Comment c : commentList){
                    CommentService.deleteCommentById(c.getId());
                }
                Comment comment = new Comment();
                comment.setOrderId(orderId);
                comment.setDetail(detail);
                comment = CommentService.addNewComment(comment);
                userLogger.info(String.format("User %d left a comment %d order.", user.getId(), orderId));
            }
            return new UserCabinetController().execute(req,resp);
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        } catch (NumberFormatException e) {
            userLogger.error("User tried enter wrong parametrs",e.fillInStackTrace());
            return "index.jsp";
        }
    }
}
