package web.service;

import web.entities.*;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.DBManager;
import web.repository.impl.*;

import java.util.List;

public class ClientService {

    public static List<Client> getAllClients() throws DBException, EntityException {
        return new ClientDAOImpl().findAll(DBManager.getInstance().getConnection(),true);
    }

    public static Client addNewClient(Client client) throws DBException, EntityException{
        return new ClientDAOImpl().insert(DBManager.getInstance().getConnection(),client);}

    public static Client getClientByUserId(Integer idUser) throws DBException, EntityException{
        return (new ClientDAOImpl().findAll(DBManager.getInstance().getConnection(),false)).stream().filter(c->c.getUserID().equals(idUser)).findFirst().get();
    }
    public static Client getClientById(Integer id) throws DBException, EntityException{
        return new ClientDAOImpl().findElementById(DBManager.getInstance().getConnection(),id,false);
    }
}
