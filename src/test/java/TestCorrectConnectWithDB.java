import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.*;
import web.exception.DBException;
import web.exception.EntityException;
import web.repository.DBManager;
import web.repository.DBManagerTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestCorrectConnectWithDB extends Assert {
    public static Connection connection = null;

    @Test
    public void openConnection()
    {
        try {
            connection = DBManager.getInstance().getConnection();
        } catch (DBException e) {
            e.printStackTrace();
        }
        assertTrue(connection != null);
    }

    @AfterClass
    public static void closeConnection()
    {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {}
    }

}
