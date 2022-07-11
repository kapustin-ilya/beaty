package web.service;


import web.entities.*;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.DBManager;
import web.repository.impl.*;

import java.util.List;

public class UserService {
    static final String SQL_FIND_USER_BY_EMAIL_AND_PASSWORD = "select * from user where email = ? and password = ?;";
    static final String SQL_FIND_USER_BY_EMAIL = "select * from user where email = ?;";

    public static User updateUser (User user) throws DBException, EntityException {
        return new UserDAOImpl().update(DBManager.getInstance().getConnection(),user);
    }

    public static List<User> getUsersByEmailAndPassword (String email,String password) throws DBException, EntityException{
        return new UserDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_USER_BY_EMAIL_AND_PASSWORD,false,email,password);
    }

    public static User addNewUserInDB (User user) throws DBException, EntityException{
        return new UserDAOImpl().insert(DBManager.getInstance().getConnection(),user);
    }

    public static User getUserById(Integer id) throws DBException, EntityException{
        return new UserDAOImpl().findElementById(DBManager.getInstance().getConnection(),id,false);
    }
    public static List<User> getUsersByEmail (String email) throws DBException, EntityException{
        return new UserDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_USER_BY_EMAIL,false,email);
    }
}
