package web.repository.impl;

import web.entities.GeneralCategory;

import java.util.Map;
import java.util.TreeMap;

public class GeneralCategoryDAOImpl extends AbstractDAOImpl<GeneralCategory>  {

    private static final Map<String, AbstractDAOImpl> entitiesDaoImpl;

    static {
        entitiesDaoImpl = new TreeMap<>();
        entitiesDaoImpl.put("category",new CategoryDAOImpl());
    }

    public GeneralCategoryDAOImpl() {
        super(GeneralCategory.class,entitiesDaoImpl);
    }
}
