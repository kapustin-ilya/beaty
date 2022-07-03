package web.service;

import web.entities.Role;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.DBManager;
import web.repository.impl.RoleDAOImpl;

import java.util.List;

public class RoleService {
    public static List<Role> getRolls () throws DBException, EntityException {
        return new RoleDAOImpl().findAll(DBManager.getInstance().getConnection(),false);
    }
}
