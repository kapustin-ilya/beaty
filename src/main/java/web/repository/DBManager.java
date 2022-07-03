package web.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import web.exception.DBException;

import javax.sql.DataSource;
import java.sql.*;

public class DBManager {

    private static DBManager dbManager;
    private static DataSource ds;
    private static BasicDataSource dataSource = new BasicDataSource();
    private static HikariConfig configHikari = null;
    private static HikariDataSource dsHikari = null;

    private DBManager() {
        try {
            /*Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            ds = (DataSource)envContext.lookup("jdbc/beauty_salon");*/

            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/beauty_salon");
            dataSource.setUsername("root");
            dataSource.setPassword("root");
            dataSource.setMaxIdle(30);
            dataSource.setMaxTotal(1000);
            dataSource.setMaxWaitMillis(10000);
            dataSource.setDefaultAutoCommit(true);

            /*configHikari = new HikariConfig("webapp/META-INF/db.properties");
            dsHikari = new HikariDataSource(configHikari);*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public static Connection getConnection() throws DBException{
        Connection cn = null;
        try {
            cn = dataSource.getConnection();
        } catch (SQLException ex) {
            throw new DBException("Can not obtain a connection", ex);
        }
        return cn;
    }
}
