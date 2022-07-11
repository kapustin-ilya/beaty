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
    private static GeneralCategoryDAOImpl generalCategoryDAOImpl = new GeneralCategoryDAOImpl();
    private static CategoryDAOImpl categoryDAOImpl = new CategoryDAOImpl();

    public static List<GeneralCategory> getAllGeneralCategories() throws DBException, EntityException {
        return generalCategoryDAOImpl.findAll(DBManager.getInstance().getConnection(), true);
    }

    public static List<Category> getAllCategories () throws DBException, EntityException{
        return categoryDAOImpl.findAll(DBManager.getInstance().getConnection(),true);
    }

    public static Category getCategoryById(Integer id) throws DBException, EntityException {
        return categoryDAOImpl.findElementById(DBManager.getInstance().getConnection(),id,true);
    }

}
