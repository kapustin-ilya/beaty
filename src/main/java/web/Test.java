package web;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Test {
    public static void main(String[] args) {


        String configFile = "src/main/resources/db.properties";
        HikariConfig cfg = new HikariConfig(configFile);
        HikariDataSource ds = new HikariDataSource(cfg);
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = ds.getConnection();
            System.out.println(con.getMetaData());
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(*) from role;");
            if(resultSet.next()){
                System.out.println(resultSet.getInt(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
