package web.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.annotation.*;
import web.controller.commads.AdminCabinetController;
import web.entities.Entity;
import lombok.Getter;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.AbstractDAO;
import web.repository.DBManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;

@Getter
public abstract class AbstractDAOImpl<E extends Entity> implements AbstractDAO<E> {

    static final Logger userLogger = LogManager.getLogger(AdminCabinetController.class);

    Class entityClass;
    String nameDB;
    Map<String, AbstractDAOImpl> entitiesDaoImpl;

    private final String SQL_FIND_ELEMENT_BY_ID;
    private final String SQL_FIND_ALL_ELEMENTS;
    private final String SQL_DELETE_ELEMENT_BY_ID;
    private final String SQL_COUNT_ELEMENT;

    private Connection cn = null;
    PreparedStatement ps = null;
    Statement st = null;
    ResultSet rs = null;

    public AbstractDAOImpl(Class<?> entityClass, Map<String, AbstractDAOImpl> entitiesDaoImpl) {
        this.entityClass = entityClass;
        this.entitiesDaoImpl = entitiesDaoImpl;
        nameDB = entityClass.getAnnotation(TableDB.class).name();
        SQL_FIND_ALL_ELEMENTS = String.format("select * from %s;",nameDB);
        SQL_FIND_ELEMENT_BY_ID = String.format("select * from %s where id = ? limit 1;",nameDB);
        SQL_DELETE_ELEMENT_BY_ID = String.format("delete from %s where id = ?;",nameDB);
        SQL_COUNT_ELEMENT = String.format("select count(*) from %s",nameDB);
    }

    @Override
    public List<E> findElementsBySQlRequest(Connection con, String SQLRequest, boolean versionOfSearch, Object...parameters) throws DBException,EntityException {
        cn = con;
        List<E> allElemetns = new ArrayList<>();
        try {
            ps = cn.prepareStatement(SQLRequest);
            for (int i = 1; i<=parameters.length;i++){
                ps.setObject(i,parameters[i-1]);
            }
            rs = ps.executeQuery();
            while (!rs.isClosed() && rs.next()) {
                allElemetns.add(readNextElement(rs, versionOfSearch));
            }
            rs.close();
            ps.close();
            cn.close();
        } catch (SQLException e) {
            throw new DBException(String.format("Exception while getting data from table %s method findElementsBySQlRequest",nameDB),e);
        } finally {
            close(rs);
            close(ps);
            close(cn);
        }
        return allElemetns;
    }

    @Override
    public E findElementById(Connection con, Integer id, boolean versionOfSearch) throws DBException,EntityException {
        cn = con;
        try {
            ps = cn.prepareStatement(SQL_FIND_ELEMENT_BY_ID);
            ps.setObject(1, id);
            rs = ps.executeQuery();
            if (!rs.isClosed() &&  rs.next()) {
                return readNextElement(rs,versionOfSearch);
            }
            rs.close();
            ps.close();
            cn.close();
        } catch (SQLException e) {
            throw new DBException(String.format("Exception while getting data from table %s method findElementById",nameDB),e);
        } finally {
            close(rs);
            close(ps);
            close(cn);
        }
        return null;
    }

    @Override
    public List<E> findAll(Connection con, boolean versionOfSearch) throws DBException,EntityException{
        cn = con;
        List<E> allElemetns = new ArrayList<>();
        try {
            st = cn.createStatement();
            rs = st.executeQuery(SQL_FIND_ALL_ELEMENTS);
            while (!rs.isClosed() &&  rs.next()) {
                allElemetns.add(readNextElement(rs, versionOfSearch));
            }
            rs.close();
            st.close();
            cn.close();
        } catch (SQLException e) {
            throw new DBException(String.format("Exception while getting data from table %s method findAll",nameDB),e);
        } finally {
            close(rs);
            close(st);
            close(cn);
        }
        return allElemetns;
    }

    @Override
    public E insert (Connection con, E entity) throws DBException, EntityException{
        cn = con;
        StringBuffer SQL_INSERT_BEGIN = new StringBuffer("insert into ");
        StringBuffer SQL_INSERT_END = new StringBuffer(") values (");
        SQL_INSERT_BEGIN.append(nameDB+"(");
        prepareSQLRequestBeforeInsOrUp(SQL_INSERT_BEGIN, SQL_INSERT_END, entity,true);
        final String SQL_INSERT_FINAL = SQL_INSERT_BEGIN.toString() + SQL_INSERT_END.toString() + ");";

        try {
            cn.setAutoCommit(false);
            cn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            ps = cn.prepareStatement(SQL_INSERT_FINAL, Statement.RETURN_GENERATED_KEYS);
            Integer parameterIndex = 1;
            parameterIndex = prepareStatementBeforeInsOrUp(ps,entity);
            int result = ps.executeUpdate();
            if (result != 0) {
                rs = ps.getGeneratedKeys();
                if (rs.isClosed() && rs.next()) {
                    entity.setId(rs.getInt(1));
                }
                insOrUpDataInTableMToM(entity,true);
            }
            cn.commit();
            rs.close();
            ps.close();
            cn.close();
        } catch ( SQLException e) {
            try {
                cn.rollback();
                cn.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new DBException(String.format("Exception while insert data from table %s method insert",nameDB),e);
            }
            throw new DBException(String.format("Exception while insert data from table %s method insert",nameDB),e);
        } finally {
            close(rs);
            close(ps);
            close(cn);
        }
        userLogger.info(String.format("Insert new data in table %s. Id - %d",nameDB,entity.getId()));
        return entity;
    }

    @Override
    public E update(Connection con, E entity) throws DBException, EntityException{
        cn = con;
        StringBuffer SQL_UPDATE_BEGIN = new StringBuffer("update ");
        StringBuffer SQL_UPDATE_END = new StringBuffer("  ");
        SQL_UPDATE_BEGIN.append(nameDB + " set ");
        prepareSQLRequestBeforeInsOrUp(SQL_UPDATE_BEGIN, SQL_UPDATE_END, entity,false);
        final String SQL_UPDATE_FINAL = SQL_UPDATE_BEGIN.toString() + " where id = ?;" ;

        try {
            cn.setAutoCommit(false);
            cn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            ps = cn.prepareStatement(SQL_UPDATE_FINAL);
            Integer parameterIndex = 1;
            parameterIndex = prepareStatementBeforeInsOrUp(ps,entity);
            ps.setInt(parameterIndex++, entity.getId());
            int result = ps.executeUpdate();
            if (result != 0){
                insOrUpDataInTableMToM(entity,false);
            }
            ps.close();
            cn.close();
        } catch ( SQLException e) {
            try {
                cn.rollback();
                cn.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new DBException(String.format("Exception while insert data from table %s method insert",nameDB),e);
            }
            throw new DBException(String.format("Exception while update data from table %s method update",nameDB),e);
        } finally {
            close(ps);
            close(cn);
        }
        userLogger.info(String.format("Update data in table %s. Id - %d",nameDB,entity.getId()));
        return entity;
    }

    @Override
    public boolean deleteById(Connection con, Integer id) throws DBException{
        cn = con;
        Boolean resultDelete = false;
        try {
            ps = cn.prepareStatement(SQL_DELETE_ELEMENT_BY_ID);
            ps.setInt(1, id);
            if (ps.executeUpdate() !=0) {
                //System.out.println("The Neverhood`s been here!!!");
                resultDelete = true;
            }
            ps.close();
            cn.close();
        } catch (SQLException e) {
            throw new DBException(String.format("Exception while delete data from table %s method deleteById",nameDB),e);
        } finally {
            close(ps);
            close(cn);
        }
        userLogger.info(String.format("Delete data in table %s. Id - %d",nameDB,id));
        return resultDelete;
    }

    @Override
    public Integer countAllElement(Connection con) throws DBException{
        Integer count = 0;
        cn = con;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(SQL_COUNT_ELEMENT);
            if (!rs.isClosed() && rs.next()){
                count = rs.getInt(1);
            }
            rs.close();
            st.close();
            cn.close();
        } catch (SQLException e) {
            throw new DBException(String.format("Exception while count data from table %s method countAllElement",nameDB),e);
        } finally {
            close(rs);
            close(ps);
            close(cn);
        }
        return count;
    }
    @Override
    public Integer countAllElement(Connection con, String SQLRequest, Object[] parameters) throws DBException{
        Integer count = 0;
        cn = con;
        try {
            ps = cn.prepareStatement(SQLRequest);
            for (int i = 1; i<=parameters.length;i++){
                ps.setObject(i,parameters[i-1]);
            }
            rs = ps.executeQuery();
            if (!rs.isClosed() && rs.next()){
                count = rs.getInt(1);
            }
            rs.close();
            ps.close();
            cn.close();
        } catch (SQLException e) {
            throw new DBException(String.format("Exception while count data from table %s method countAllElement with parametrs",nameDB),e);
        } finally {
            close(rs);
            close(ps);
            close(cn);
        }
        return count;
    }

    private E readNextElement(ResultSet resultSet, boolean versionOfSearch) throws DBException,EntityException{
        E element = null;
        try {
            Constructor<E> cons = entityClass.getConstructor();
            element = (E) cons.newInstance();

            element.setId(resultSet.getInt("id"));
            for (Field field : entityClass.getDeclaredFields()){
                if (field.getAnnotation(Column.class) != null){
                    String nameColumnDB = field.getAnnotation(Column.class).name();

                    field.setAccessible(true);
                    field.set(element, resultSet.getObject(nameColumnDB,field.getType()));
                    continue;
                }
                if (field.getAnnotation(ManyToOne.class) != null && versionOfSearch){
                    String foreignColumnDB = field.getAnnotation(ManyToOne.class).foreignColumnDB();
                    Integer foriegnID = resultSet.getInt(foreignColumnDB);

                    AbstractDAOImpl foreignEntityDAOImpl = entitiesDaoImpl.get(field.getAnnotation(ManyToOne.class).foreignTable());

                    Object foreignElement = foreignEntityDAOImpl.findElementById(DBManager.getInstance().getConnection(), foriegnID,false);

                    field.setAccessible(true);
                    field.set(element, foreignElement);
                    continue;
                }
                if (field.getAnnotation(OneToMany.class) != null && versionOfSearch){
                    String foreignColumnDB = field.getAnnotation(OneToMany.class).mainColumnDB();
                    Integer elementId = element.getId();

                    AbstractDAOImpl foreignEntityDAOImpl = entitiesDaoImpl.get(field.getAnnotation(OneToMany.class).foreignTable());

                    Set<Object> setForeignElements = foreignEntityDAOImpl.FindAllForOneToMany(elementId, foreignColumnDB,false);

                    field.setAccessible(true);
                    field.set(element, setForeignElements);
                    continue;
                }
                if (field.getAnnotation(ManyToMany.class) != null && versionOfSearch){
                    Integer elementId = (Integer) element.getId();

                    String tableForManyToMany = field.getAnnotation(ManyToMany.class).tableForManyToMany();
                    String mainColumnDB = field.getAnnotation(ManyToMany.class).mainColumnDB();
                    String foreignColumnDB = field.getAnnotation(ManyToMany.class).foreignColumnDB();

                    AbstractDAOImpl foreignEntityDAOImpl = entitiesDaoImpl.get(field.getAnnotation(ManyToMany.class).foreignTable());

                    Set<Integer> idForeignElements = FindAllForManyToMany(elementId,tableForManyToMany,mainColumnDB,foreignColumnDB);
                    Set<Object> setForeignElements = new HashSet<>();
                    /*setForeignElements = idForeignElements.stream()
                                    .map(index->foreignEntityDAOImpl.findElementById(DBManager.getInstance().getConnection(), index,false))
                                    .collect(Collectors.toSet());*/
                    for(Integer idForeignElement : idForeignElements){
                        setForeignElements.add(foreignEntityDAOImpl.findElementById(DBManager.getInstance().getConnection(), idForeignElement,false));
                    }

                    field.setAccessible(true);
                    field.set(element, setForeignElements);
                    continue;
                }
            }
        } catch (InstantiationException /*cons.newInstance();*/
                | IllegalAccessException /*cons.newInstance();*/
                | InvocationTargetException /*cons.newInstance();*/
                | NoSuchMethodException /*entityClass.getConstructor();*/ e ) {
            throw new EntityException(String.format("Exception with work %s entity", entityClass.getName()),e);
        } catch (SQLException e){
            throw new DBException(String.format("Exception while getting data from table %s method readNextElement",nameDB),e);
        }
        return element;
    }

    private Set<E> FindAllForOneToMany(Integer elementId, String foreignColumnDB, boolean versionOfSearch) throws DBException, EntityException{
        Set<E> setForeignElements = new HashSet<>();
        final String FIND_ALL_BY_FOREIGN_ID = String.format("select * from %s where %s = ?;",nameDB,foreignColumnDB);
        try {
            Connection innerCn = DBManager.getInstance().getConnection();
            PreparedStatement innerPs = innerCn.prepareStatement(FIND_ALL_BY_FOREIGN_ID);
            innerPs.setInt(1,elementId);
            ResultSet resSet = innerPs.executeQuery();
            while (!resSet.isClosed() && resSet.next()) {
                setForeignElements.add(readNextElement(resSet,versionOfSearch));
            }
            resSet.close();
            innerPs.close();
         } catch (SQLException e) {
            throw new DBException(String.format("Exception while getting data from table %s method FindAllForOneToMany",nameDB),e);
        }

        return setForeignElements;
    }

    private Set<Integer> FindAllForManyToMany(Integer elementId, String tableForManyToMany, String mainColumnDB, String foreignColumnDB) throws DBException{
        Set<Integer> idForeignElements = new HashSet<>();
        final String FIND_ALL_IN_MToM_BY_FOREIGN_ID = String.format("select %s from %s where %s = ?;",foreignColumnDB,tableForManyToMany,mainColumnDB);
        try {
            Connection innerCn = DBManager.getInstance().getConnection();
            PreparedStatement innerPs = innerCn.prepareStatement(FIND_ALL_IN_MToM_BY_FOREIGN_ID);
            innerPs.setInt(1,elementId);
            ResultSet resSet = innerPs.executeQuery();
            while (!resSet.isClosed() && resSet.next()){
                idForeignElements.add(resSet.getInt(foreignColumnDB));
            }
            resSet.close();
            innerPs.close();
       } catch (SQLException e) {
            throw new DBException(String.format("Exception while getting data from table %s method FindAllForManyToMany",nameDB),e);
       }
        return idForeignElements;
    }

    private void prepareSQLRequestBeforeInsOrUp(StringBuffer SQL_BEGIN, StringBuffer SQL_END, E entity, boolean typeOperation) throws EntityException{
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getAnnotation(Column.class) != null) {
                field.setAccessible(true);
                try {
                    if (field.get(entity) != null) {
                        if (typeOperation) {
                            SQL_BEGIN.append(field.getAnnotation(Column.class).name() + ", ");
                            SQL_END.append("?, ");
                        } else {
                            SQL_BEGIN.append(field.getAnnotation(Column.class).name() + " = ?, ");
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new EntityException(String.format("Exception with work %s entity", entityClass.getName()),e);
                }
            }
        }
        SQL_BEGIN.delete(SQL_BEGIN.length()-2, SQL_BEGIN.length());
        SQL_END.delete(SQL_END.length()-2,SQL_END.length());
    }

    private Integer prepareStatementBeforeInsOrUp(PreparedStatement ps, E entity ) throws EntityException,DBException {
        Integer parameterIndex = 1;
        try {
            for (Field field : entityClass.getDeclaredFields()) {
                field.setAccessible(true);
                    if (field.getAnnotation(Column.class) != null && field.get(entity) != null) {
                        ps.setObject(parameterIndex++, field.get(entity));
                    continue;
                }
            }
        } catch (IllegalAccessException e) {
            throw new EntityException(String.format("Exception with work %s entity", entityClass.getName()),e);
        }catch (SQLException e) {
            throw new DBException(String.format("Exception with work %s entity", entityClass.getName()),e);
        }

        return parameterIndex;
    }

    private void insOrUpDataInTableMToM (E entity, boolean typeOperation ) throws EntityException,DBException{
        PreparedStatement psMtoM = null;
        try {
        for (Field field : entityClass.getDeclaredFields()){
            field.setAccessible(true);
            if (field.getAnnotation(ManyToMany.class) != null && field.get(entity) != null) {
                Set<Entity> setForeignElements = (Set<Entity>) field.get(entity);
                Integer elementId = (Integer) entity.getId();

                String tableForManyToMany = field.getAnnotation(ManyToMany.class).tableForManyToMany();
                String mainColumnDB = field.getAnnotation(ManyToMany.class).mainColumnDB();
                String foreignColumnDB = field.getAnnotation(ManyToMany.class).foreignColumnDB();

                if (!typeOperation) {
                    final String DELETE_DATA_IN_MToM_BEFORE_INSERT = String.format("delete from %s where %s = ?", tableForManyToMany, mainColumnDB);
                    psMtoM = cn.prepareStatement(DELETE_DATA_IN_MToM_BEFORE_INSERT);
                    psMtoM.setObject(1, elementId);
                    psMtoM.executeUpdate();
                }

                final String INSERT_DATA_IN_MToM = String.format("insert into %s (%s, %s) values (%s, ?);",tableForManyToMany,mainColumnDB,foreignColumnDB, elementId.toString());

                for (Entity foreignElement : setForeignElements){
                    psMtoM = cn.prepareStatement(INSERT_DATA_IN_MToM);
                    psMtoM.setObject(1,foreignElement.getId());
                    psMtoM.executeUpdate();
                }
                continue;
            }
        } } catch (IllegalAccessException e) {
            throw new EntityException(String.format("Exception with work %s entity", entityClass.getName()),e);
        }catch (SQLException e) {
            throw new DBException(String.format("Exception with work %s entity", entityClass.getName()),e);
        }
    }

    private void close(Connection con) throws DBException{
        if (con != null){
            try {
                con.close();
            } catch (SQLException e) {
                throw new DBException(String.format("Exception while close connection with table %s",nameDB),e);
            }
        }
    }
    private void close(PreparedStatement ps) throws DBException{
        if (ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                throw new DBException(String.format("Exception while close preparedStatement with table %s",nameDB),e);
            }
        }
    }
    private void close(Statement st) throws DBException{
        if (st != null){
            try {
                st.close();
            } catch (SQLException e) {
                throw new DBException(String.format("Exception while close statement with table %s",nameDB),e);
            }
        }
    }
    private void close(ResultSet rs) throws DBException{
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DBException(String.format("Exception while close resultSet with table %s",nameDB),e);
            }
        }
    }
}
