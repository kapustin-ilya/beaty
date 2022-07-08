import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManagerTest {
    private static DBManagerTest dbManagerTest = null;
    private static BasicDataSource dataSource = new BasicDataSource();

    public DBManagerTest() {
    }

    public static DBManagerTest getInstance() {
        if (dbManagerTest == null) {
            dbManagerTest = new DBManagerTest();
        }
        return dbManagerTest;
    }

    public Connection getConnection() {
        /*HikariConfig configHikari = new HikariConfig();
        configHikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
        configHikari.setJdbcUrl( "jdbc:mysql://localhost:3306/beauty_salon_test" );
        configHikari.setUsername( "root" );
        configHikari.setPassword( "root" );
        configHikari.setMaximumPoolSize( 1000 );
        configHikari.setAutoCommit(true);
        HikariDataSource dsHikari = new HikariDataSource(configHikari);*/

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/beauty_salon_test");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setMaxIdle(30);
        dataSource.setMaxTotal(1000);
        dataSource.setMaxWaitMillis(10000);
        dataSource.setDefaultAutoCommit(true);

        Connection cn = null;
        try {
            cn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cn;
    }
}
