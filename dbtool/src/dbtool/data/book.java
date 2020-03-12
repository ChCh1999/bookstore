package dbtool.data;

import dbtool.DBException;

import java.sql.*;
public class book{
    private int id;
    public String name;
    public String publisher;
    public String imgPath;
    public String info;
    public int storeCount;
    private Connection conn;
    public book(int id,String name){
        this.id=id;
        this.name=name;
    }
    public book(int id,String name,Connection conn){
        this.id=id;
        this.name=name;
        this.conn=conn;
    }

    public int getId() {
        return id;
    }
    public boolean updata(){
        if(conn==null)
            return false;
        try {
            PreparedStatement psmt=conn.prepareStatement("UPDATE book SET name=?,publisher=?,storeCount=?,img=?,info=? WHERE id=?");
            psmt.setString(1,name);
            psmt.setString(2,publisher);
            psmt.setInt(3,storeCount);
            psmt.setString(4,imgPath);
            psmt.setString(5,info);
            psmt.setInt(1,this.id);
            psmt.executeUpdate();
            psmt.close();
            return true;
        }catch (SQLException sqle){
            System.out.println(sqle);
            return false;
        }
    }
    public boolean delete(){
        if(conn==null)
            return false;
        try {
            PreparedStatement psmt=conn.prepareStatement("DELETE FROM book WHERE id=?");
            psmt.setInt(1,this.id);
            psmt.executeUpdate();
            psmt.close();
            return true;
        }catch (SQLException sqle){
            System.out.println(sqle);
            return false;
        }

    }
}