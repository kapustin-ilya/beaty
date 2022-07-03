package web.repository;

import web.exception.DBException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManagerTest {
    private static DBManagerTest dbManagerTest;

    static String username = "root";
    static String password = "root";

    private DBManagerTest() {
    }

    public static synchronized DBManagerTest getInstance() {
        if (dbManagerTest == null) {
            dbManagerTest = new DBManagerTest();
        }
        return dbManagerTest;
    }

    public Connection getConnection() {
        Connection cn = null;
        try {
            cn = DriverManager.getConnection(String.format("jdbc:mysql://localhost:3306/beauty_salon?user=%s&password=%s",username,password));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cn;
    }
}
