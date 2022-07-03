package web.service;

import web.entities.Comment;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.DBManager;
import web.repository.impl.CommentDAOImpl;

import java.util.List;

public class CommentService {
    static final String SQL_FIND_COMMENT_BY_ORDER_ID = "select * from comment where order_id= ? ;";

    public static Comment addNewComment(Comment comment) throws DBException, EntityException {
        return new CommentDAOImpl().insert(DBManager.getInstance().getConnection(),comment);
    }
    public static List<Comment> getComments() throws DBException, EntityException{
        return new CommentDAOImpl().findAll(DBManager.getInstance().getConnection(),true);
    }
    public static List<Comment> getCommentsByOrderId(Integer orderId) throws DBException, EntityException{
        return new CommentDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_COMMENT_BY_ORDER_ID,false, orderId);
    }

    public static boolean deleteCommentById (Integer id) throws DBException, EntityException{
        return new CommentDAOImpl().deleteById(DBManager.getInstance().getConnection(),id);
    }
}
