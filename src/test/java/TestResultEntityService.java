
import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import web.entities.GeneralCategory;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.DBManager;
import web.service.CategoryService;

import java.util.List;

public class TestResultEntityService extends Assert {

    @Test
    public void resultCategoryService() throws DBException, EntityException  {
        try (MockedStatic<DBManager> theMock = Mockito.mockStatic(DBManager.class)) {
            theMock.when(DBManager::getConnection).thenReturn(DBManagerTest.getInstance().getConnection());

            List <GeneralCategory> generalCategoryList = CategoryService.getAllGeneralCategories();
            assertEquals(2,generalCategoryList.size());
        }
    }
}
