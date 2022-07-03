package web.service;

import web.entities.Category;
import web.entities.GeneralCategory;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.DBManager;
import web.repository.impl.CategoryDAOImpl;
import web.repository.impl.GeneralCategoryDAOImpl;

import java.util.List;

public class CategoryService {

    public static List<GeneralCategory> getAllGeneralCategories() throws DBException, EntityException {
        return new GeneralCategoryDAOImpl().findAll(DBManager.getInstance().getConnection(), true);
    }

    public static List<Category> getAllCategories () throws DBException, EntityException{
        return new CategoryDAOImpl().findAll(DBManager.getInstance().getConnection(),true);
    }

    public static Category getCategoryById(Integer id) throws DBException, EntityException {
        return new CategoryDAOImpl().findElementById(DBManager.getInstance().getConnection(),id,true);
    }

}
