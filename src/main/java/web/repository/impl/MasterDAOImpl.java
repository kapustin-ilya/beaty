package web.repository.impl;

import web.entities.Master;

import java.util.Map;
import java.util.TreeMap;

public class MasterDAOImpl extends AbstractDAOImpl<Master>{

    private static final Map<String, AbstractDAOImpl> entitiesDaoImpl;

    static {
        entitiesDaoImpl = new TreeMap<>();
        entitiesDaoImpl.put("user",new UserDAOImpl());
        entitiesDaoImpl.put("category",new CategoryDAOImpl());
        entitiesDaoImpl.put("order",new OrderDAOImpl());
    }

    public MasterDAOImpl() {
        super(Master.class, entitiesDaoImpl);

    }
}
