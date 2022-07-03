package web.repository.impl;

import web.entities.Category;
import web.repository.DBManager;

import java.util.Map;
import java.util.TreeMap;

public class CategoryDAOImpl extends AbstractDAOImpl<Category> {

    private static final Map<String, AbstractDAOImpl> entitiesDaoImpl;

    static {
        entitiesDaoImpl = new TreeMap<>();
        entitiesDaoImpl.put("general_category",new GeneralCategoryDAOImpl());
        entitiesDaoImpl.put("master",new MasterDAOImpl());
    }

    public CategoryDAOImpl() {
        super(Category.class, entitiesDaoImpl);
    }

}
