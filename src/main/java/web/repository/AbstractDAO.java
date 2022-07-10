package web.repository;

import web.entities.Entity;
import web.exception.DBException;
import web.exception.EntityException;

import java.sql.Connection;
import java.util.List;

public interface AbstractDAO<E extends Entity> {

    public List<E> findAll(Connection con, boolean versionOfSearch) throws DBException, EntityException;
    public E findElementById(Connection con, Integer id, boolean versionOfSearch) throws DBException, EntityException;
    public List<E> findElementsBySQlRequest(Connection con, String SQLReques, boolean versionOfSearch, Object...parameters) throws DBException, EntityException;

    public E insert (Connection con, E entity) throws DBException, EntityException;
    public E update (Connection con, E entity) throws DBException, EntityException;

    public boolean deleteById (Connection con, Integer id) throws DBException;

    public Integer countAllElement (Connection con) throws DBException;
    public Integer countAllElement (Connection con, String SQLRequest, Object...parameters) throws DBException;
}
