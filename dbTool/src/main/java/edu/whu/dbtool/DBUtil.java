package edu.whu.dbtool;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Ch
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class DBUtil{
    private static DataSource ds = null;

    static {
        try{
            InputStream in = DBUtil.class.getClassLoader()
                    .getResourceAsStream("static/jdbc.properties");
            Properties props = new Properties();
            props.load(in);
            in.close();
            ds = DruidDataSourceFactory.createDataSource(props);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void main(String[] args) {
        try {
            Connection conn=DBUtil.ds.getConnection();
            System.out.println("success");
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
