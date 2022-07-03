package web.repository.impl;

import web.entities.Order;

import java.util.Map;
import java.util.TreeMap;

public class OrderDAOImpl extends AbstractDAOImpl<Order>{


    private static final Map<String, AbstractDAOImpl> entitiesDaoImpl;

    static {
        entitiesDaoImpl = new TreeMap<>();
        entitiesDaoImpl.put("master",new MasterDAOImpl());
        entitiesDaoImpl.put("client",new ClientDAOImpl());
        entitiesDaoImpl.put("category",new CategoryDAOImpl());
    }

    public OrderDAOImpl() {
        super(Order.class, entitiesDaoImpl);
    }
}
