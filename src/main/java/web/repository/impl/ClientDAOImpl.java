package web.repository.impl;

import web.entities.Client;

import java.util.Map;
import java.util.TreeMap;

public class ClientDAOImpl extends AbstractDAOImpl<Client>{
    private static final Map<String, AbstractDAOImpl> entitiesDaoImpl;

    static {
        entitiesDaoImpl = new TreeMap<>();
        entitiesDaoImpl.put("user",new UserDAOImpl());
        entitiesDaoImpl.put("order",new OrderDAOImpl());
    }

    public ClientDAOImpl() {
        super(Client.class, entitiesDaoImpl);
    }
}
