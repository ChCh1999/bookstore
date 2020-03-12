package dbtool;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dbtool.data.*;
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
                System.out.println("Succeeded connecting to the Database" + dbUrl);
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
            e.printStackTrace(); //handle other exception
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(stmt!=null){
            stmt.close();
        }
        if(conn!=null){
            conn.commit();
            conn.close();
        }
    }

    //增

    /**
     * 添加图书
     *
     * @param bookName   图书名称
     * @param publisher  出版社
     * @param storeCount 图书库存
     * @return 成功返回true 否则返回false
     * @throws DBException
     */
    public boolean addBook(String bookName, String publisher, int storeCount) throws DBException {

        try {
            PreparedStatement psmt = conn.prepareStatement("INSERT INTO book(name, publisher, storeCount) VALUE (?,?,?);");
            psmt.setString(1, bookName);
            psmt.setString(2, publisher);
            psmt.setInt(3, storeCount);
            psmt.executeUpdate();
        } catch (SQLException sqle) {
            System.out.println(sqle);
            throw (new DBException(sqle.getMessage()));
//            return false;
        }
        return true;
    }
    /**
     * 添加图书
     *
     * @param bookName   图书名称
     * @param publisher  出版社
     * @param storeCount 图书库存
     * @param imgPath 图片路径
     * @return 成功返回true 否则报错
     *         图片不存在-》DBException("img not found");
     *         数据库操作错误-》 DBException(sqle.getMessage())；
     * @throws DBException
     */
    public boolean addBook(String bookName, String publisher, int storeCount, String imgPath) throws DBException {

        try {
            //检查图片路径
            File img =new File(imgPath);
            if(!img.exists())
                throw  new DBException("img not found");
            PreparedStatement psmt = conn.prepareStatement("INSERT INTO book(name, publisher, storeCount) VALUE (?,?,?);");
            psmt.setString(1, bookName);
            psmt.setString(2, publisher);
            psmt.setInt(3, storeCount);
            psmt.executeUpdate();
        } catch (SQLException sqle) {
            System.out.println(sqle);
            throw (new DBException(sqle.getMessage()));
//            return false;
        }
        return true;
    }


    //删
//    public boolean deleteXXX()throws DBException{
//        try {
//            PreparedStatement psmt = conn.prepareStatement("DELETE FROM ? WHERE ");
//            psmt.setString(1, bookName);
//            psmt.executeUpdate();
//        } catch (SQLException sqle) {
//            System.out.println(sqle);
//            throw (new DBException(sqle.getMessage()));
//        }
//        return true;
//    }
    public boolean deleteBook(int bookID) throws DBException{
        try {
            PreparedStatement psmt = conn.prepareStatement("DELETE FROM book WHERE id=?");
            psmt.setInt(1,bookID);
            psmt.executeUpdate();
        } catch (SQLException sqle) {
            System.out.println(sqle);
            throw (new DBException(sqle.getMessage()));
        }
        return true;
    }

    //改
    public boolean changeBookStore(){
        //TODO:执行更改
        return true;
    }

    //查
    public List<book> searchBookByName(String bookName) throws DBException {
        try {
            PreparedStatement psmt=conn.prepareStatement("SELECT * FROM book WHERE name = ?");
            psmt.setString(1,bookName);
            ResultSet rs=psmt.executeQuery();
            List<book> res=new ArrayList<>();
            while (rs.next()){

                String resName=rs.getString("name");
                int resId=rs.getInt("id");
                book temp=new book(resId,resName,this.conn);
                temp.imgPath=rs.getString("img");
                temp.info=rs.getString("info");
                temp.publisher=rs.getString("publisher");
                temp.storeCount=rs.getInt("storeCount");
                res.add(temp);
            }
            return res;
        }catch (SQLException sqle){
            System.out.println(sqle);
            throw new DBException(sqle.getMessage());
        }
    }

    public static void main(String[] args) {
        DBTool dt = new DBTool();
        try {
            dt.addBook("语文", "人民出版社", 100);
            List<book> res=dt.searchBookByName("语文");
            for(book b : res){
                System.out.println(b.getId());
//                dt.deleteBook(b.id);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
