package dbtool.data;

import java.sql.*;
public class user{
    public String account;
    public String name;
    public String password;
    public String address;
    public String province;
    public String city;
    public String info;
    public float money;
    private Connection conn;

    public user(String account,String pw){
        this.account=account;
        this.password=pw;
    }
   public user(String account,String pw,Connection conn){
        this.account=account;
        this.password=pw;
        this.conn=conn;
    }
    public boolean updata(){
        if(conn==null)
            return false;
        try {
            PreparedStatement psmt=conn.prepareStatement("UPDATE user SET name=?, password=?, address=?, province=?, city=?, info=?, money=? WHERE account=?");
            psmt.setString(1,name);
            psmt.setString(2,password);
            psmt.setString(3,address);
            psmt.setString(4,province);
            psmt.setString(5,city);
            psmt.setString(6,info);
            psmt.setFloat(7,money);
            psmt.setString(8,account);
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
            PreparedStatement psmt=conn.prepareStatement("DELETE FROM user  WHERE account=?");
            psmt.setString(1,this.account);
            psmt.executeUpdate();
            psmt.close();
            return true;
        }catch (SQLException sqle){
            System.out.println(sqle);
            return false;
        }

    }
}