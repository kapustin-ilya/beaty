package web.repository.impl;

import web.entities.Role;

import java.util.Map;
import java.util.TreeMap;

public class RoleDAOImpl extends AbstractDAOImpl<Role>{

    private static final Map<String, AbstractDAOImpl> entitiesDaoImpl;

    static {
        entitiesDaoImpl = new TreeMap<>();
    }

    public RoleDAOImpl() {
        super(Role.class,entitiesDaoImpl);
    }
}
