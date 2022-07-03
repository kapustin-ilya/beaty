package web.repository.impl;

import web.entities.User;

import java.util.Map;
import java.util.TreeMap;

public class UserDAOImpl extends AbstractDAOImpl<User>{
    private static final Map<String, AbstractDAOImpl> entitiesDaoImpl;

    static {
        entitiesDaoImpl = new TreeMap<>();
        entitiesDaoImpl.put("role",new RoleDAOImpl());

    }
    public UserDAOImpl() {
        super(User.class,entitiesDaoImpl);
    }
}
