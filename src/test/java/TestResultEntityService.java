
import org.junit.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import web.entities.*;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.DBManager;
import web.service.CategoryService;
import web.service.ClientService;
import web.service.CommentService;
import web.service.MasterService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestResultEntityService extends Assert {
    List<Connection> connections = null;


    @Before
    public void init (){
        connections = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
                connections.add((new DBManagerTest()).getConnection());
        }
    }


    @After
    public void destroy (){
        for (int i = 0; i < connections.size(); i++) {
            try {
                if (connections.get(i) !=null) {
                    connections.get(i).close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void resultCategoryServiceGetAllCategories() throws DBException, EntityException  {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());
            List <Category> categoryList = CategoryService.getAllCategories();
            assertEquals(4,categoryList.size());
        }
    }

    @Test
    public void resultCategoryServiceGetAllGeneralCategories() throws DBException, EntityException  {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());
            List <GeneralCategory> generalCategoryList = CategoryService.getAllGeneralCategories();
            assertEquals(2,generalCategoryList.size());
        }
    }

    @Test
    public void resultCategoryServiceGetAllCategoriesGetCategoryById() throws DBException, EntityException  {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());
            Category category = CategoryService.getCategoryById(1);
            assertEquals("стрижка женская",category.getName());
        }
    }

    //-----------------------------------------------------------------

    @Test
    public void resultClientServiceGetAllClients() throws DBException, EntityException  {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());
            List<Client> clientList = ClientService.getAllClients();
            assertEquals(2,clientList.size());
        }
    }

    @Test
    public void resultClientServiceGetClientByUserId() throws DBException, EntityException  {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());
            Client client = ClientService.getClientByUserId(1);
            assertNull(client);
        }
    }

    @Test
    public void resultClientServiceGetClientById() throws DBException, EntityException  {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());
            Client client = ClientService.getClientById(1);
            assertEquals(Integer.valueOf(4), client.getUserID());
        }
    }
    //-----------------------------------------------------------------

    @Test
    public void resultCommentServiceGetComments() throws DBException, EntityException  {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());
            List<Comment> commentList = CommentService.getComments();
            assertEquals(2, commentList.size());
        }
    }

    @Test
    public void resultCommentServiceGetCommentsByOrderId() throws DBException, EntityException  {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());
            List<Comment> commentList = CommentService.getCommentsByOrderId(1);
            assertEquals(1, commentList.size());
        }
    }

   @Test
    public void resultCommentServiceAddNewCommentAndDeleteCommentById()  {
       try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
           theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());

           Comment newComment = new Comment();
           newComment.setDetail("Test3");
           newComment.setOrderId(1);
           newComment = CommentService.addNewComment(newComment);
           assertNotNull(newComment.getId());

           theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());

           assertTrue(CommentService.deleteCommentById(newComment.getId()));

           theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());

           List<Comment> commentList = CommentService.getComments();
           assertEquals(2, commentList.size());

        } catch(DBException | EntityException e){
        e.printStackTrace();
       }
   }

    @Test
    public void resultMasterServiceGetAllMaster() throws DBException, EntityException {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());

            List<Master> masterList = MasterService.getAllMaster();
            assertEquals(2, masterList.size());
        }

    }
    @Test
    public void resultMasterServiceGetMasterById() throws DBException, EntityException {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());
            Master master = MasterService.getMasterById(1);
            assertEquals(Integer.valueOf(2), master.getUserId());
        }

    }



   @Test
   public void resultMasterServiceUpdateMaster() throws DBException, EntityException{
       try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
           theMock.when(DBManager::getConnection).thenReturn((new DBManagerTest()).getConnection(), connections.toArray());
           Master master = MasterService.getMasterById(1);
           master.setLevelQuality("hight");
           master = MasterService.updateMaster(master);
           master = MasterService.getMasterById(1);
           assertEquals("hight", master.getLevelQuality());
       }

    }
}

    /*public static Master updateMaster (Master master) throws DBException, EntityException {
        return new MasterDAOImpl().update(DBManager.getInstance().getConnection(),master);
    }

    +++++++public static List<Master> getAllMaster () throws DBException, EntityException{
        return new MasterDAOImpl().findAll(DBManager.getInstance().getConnection(),true);
    }

    public static List<Master> getAllMasterByCategory(Integer categoryId) throws DBException, EntityException{
        return new MasterDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_MASTER_BY_CATEGORY_ID, true, categoryId);
    }
    public static List<Master> getAllMasterByGeneralCategory(Integer generalCategoryId) throws DBException, EntityException{
        return new MasterDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_MASTER_BY_GENERAL_CATEGORY_ID, true, generalCategoryId);
    }

    public static List<Master> getAllMasterByName(String name) throws DBException, EntityException{
        return new MasterDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_MASTER_BY_NAME, true, "%" + name + "%");
    }
    +++++++++public static Master getMasterById(Integer masterId) throws DBException, EntityException{
        return new MasterDAOImpl().findElementById(DBManager.getInstance().getConnection(),masterId,true);
    }

    public static Master getMasterByUserId(Integer userId) throws DBException, EntityException{
        List<Master> masterList = new MasterDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),SQL_FIND_MASTER_BY_USER_ID,true, userId);
        return masterList.size()>0?masterList.get(0):null;
    }

    public static Integer countMaster(String SQLRequest, Object[] parameters) throws DBException, EntityException{
        return new MasterDAOImpl().countAllElement(DBManager.getInstance().getConnection(),SQLRequest, parameters );

    }

    public static List<Master> getAllMasterBySQLRequest(String sqlRequest, Object[] parametrs) throws DBException, EntityException{
        return new MasterDAOImpl().findElementsBySQlRequest(DBManager.getInstance().getConnection(),sqlRequest, true, parametrs);
    }

    public static Master addNewMaster(Master Master) throws DBException, EntityException{
        return new MasterDAOImpl().insert(DBManager.getInstance().getConnection(),Master);}*/