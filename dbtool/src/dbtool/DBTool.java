package dbtool;

import java.sql.*;

public class DBTool {
    private Connection conn = null;
    private Statement stmt = null;

    //    private String dbUrl = "118.31.58.31:3306/bookstore";
    private String dbUrl = "localhost/bookstore";
    private String dbUser = "conn";
    private String dbPW = "chaos123";

    public DBTool() {
        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //getConnecting（）方法，用来连接mysql的数据库
            conn = DriverManager.getConnection("jdbc:mysql://" + dbUrl + "?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true  ", dbUser, dbPW);
            if (!conn.isClosed()) {
                System.out.println("Succeeded connecting to the Database");
            }
            //创建statement 对象 ，用来执行sql 语句
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("can't find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace(); //handle exception
        }
    }


    //增
    /**
     * 添加图书
     * @param bookName 图书名称
     * @param publisher
     * @param storeCount
     * @return
     * @throws SQLException
     */
    public boolean addBook(String bookName, String publisher, int storeCount) throws DBException{

        try {
            PreparedStatement psmt = conn.prepareStatement("INSERT INTO book(name,publisher,storeCount) VALUE (?,?,?);");
            psmt.setString(1, bookName);
            psmt.setString(2, publisher);
            psmt.setInt(3, storeCount);
            psmt.executeUpdate();
        } catch (SQLException sqle) {
            System.out.println(sqle);
            throw(new DBException(sqle.getMessage()));
//            return false;
        }
        return true;
    }



    //删
    //改
    //查
    public boolean searchBook(){
        return true;
    }
    public static void main(String[] args) {
        DBTool dt = new DBTool();
        try {
            dt.addBook("语文","人民出版社",100);
        }catch (SQLException e){
            System.out.println(e);
        }
    }
}
