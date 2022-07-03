package web.repository.impl;

import web.entities.Comment;

import java.util.Map;
import java.util.TreeMap;

public class CommentDAOImpl extends AbstractDAOImpl<Comment>{


    private static final Map<String, AbstractDAOImpl> entitiesDaoImpl;

    static {
        entitiesDaoImpl = new TreeMap<>();
        entitiesDaoImpl.put("order",new OrderDAOImpl());
    }

    public CommentDAOImpl() {
        super(Comment.class, entitiesDaoImpl);
    }

}
