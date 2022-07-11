package web.service;


import web.entities.Client;
import web.entities.Master;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.DBManager;
import web.repository.impl.ClientDAOImpl;
import web.repository.impl.MasterDAOImpl;

import java.util.List;

public class MasterService {

    static final String SQL_FIND_MASTER_BY_NAME = "select * from master where user_id = any (select id from user where name like ?);";

    static final String SQL_FIND_MASTER_BY_USER_ID = "select * from master where user_id = ? limit 1;";

    static final String SQL_FIND_MASTER_BY_CATEGORY_ID = "select * from master where id = any " +
            "(select master_id from master_has_category where category_id = any (select id from category where id = ?));";

    static final String SQL_FIND_MASTER_BY_GENERAL_CATEGORY_ID = "select * from master where id = any " +
            "(select master_id from master_has_category where category_id = any (select id from category where general_category_id = ?));";

    public static Master updateMaster (Master master) throws DBException, EntityException {
        return new MasterDAOImpl().update(DBManager.getInstance().getConnection(),master);
    }

    public static List<Master> getAllMaster () throws DBException, EntityException{
        return new MasterDAOImpl().findAll(DBManager.getInstance().getConnection(),true);
    }

    public static List<Master> getAllMasterByCategory(Integer categoryId) throws DBException, EntityException{
        return new MasterDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_MASTER_BY_CATEGORY_ID, true, categoryId);
    }
    public static List<Master> getAllMasterByGeneralCategory(Integer generalCategoryId) throws DBException, EntityException{
        return new MasterDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_MASTER_BY_GENERAL_CATEGORY_ID, true, generalCategoryId);
    }

    public static List<Master> getAllMasterByName(String name) throws DBException, EntityException{
        return new MasterDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_MASTER_BY_NAME, true, "%" + name + "%");
    }
    public static Master getMasterById(Integer masterId) throws DBException, EntityException{
        return new MasterDAOImpl().findElementById(DBManager.getInstance().getConnection(),masterId,true);
    }
    public static Master getMasterByUserId(Integer userId) throws DBException, EntityException{
        List<Master> masterList = new MasterDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_MASTER_BY_USER_ID,true, userId);
        return masterList.size()>0?masterList.get(0):null;
    }
    public static Integer countMaster(String SQLRequest, Object[] parameters) throws DBException, EntityException{
        return new MasterDAOImpl().countAllElement(DBManager.getInstance().getConnection(),SQLRequest, parameters );

    }
    public static List<Master> getAllMasterBySQLRequest(String sqlRequest, Object[] parametrs) throws DBException, EntityException{
        return new MasterDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),sqlRequest, true, parametrs);
    }
    public static Master addNewMaster(Master Master) throws DBException, EntityException{
        return new MasterDAOImpl().insert(DBManager.getInstance().getConnection(),Master);}
}
