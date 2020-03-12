package dbtool.data;

import java.sql.*;

public class order {
    public int id;
    public int bookID;
    public String userAccount;
    public int count;
    private Connection conn;

    public order(int id, int bookID, String userAccount, int count) {
        this.id = id;
        this.bookID = bookID;
        this.userAccount = userAccount;
        this.count = count;
    }

    public order(int id, int bookID, String userAccount, int count, Connection conn) {
        this.id = id;
        this.bookID = bookID;
        this.userAccount = userAccount;
        this.count = count;
        this.conn = conn;
    }

    public boolean updata() {
        if (conn == null)
            return false;
        try {
            PreparedStatement psmt = conn.prepareStatement("UPDATE order SET bookID=?,userAccount=? WHERE id=?");
            psmt.setInt(1, bookID);
            psmt.setString(2, userAccount);
            psmt.setInt(3, id);
            psmt.executeUpdate();
            psmt.close();
            return true;
        } catch (SQLException sqle) {
            System.out.println(sqle);
            return false;
        }
    }

    public boolean delete() {
        if (conn == null)
            return false;
        try {
            PreparedStatement psmt = conn.prepareStatement("DELETE FROM order WHERE id=?");
            psmt.setInt(1, this.id);
            psmt.executeUpdate();
            psmt.close();
            return true;
        } catch (SQLException sqle) {
            System.out.println(sqle);
            return false;
        }

    }
}