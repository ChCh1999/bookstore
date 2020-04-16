package edu.whu.bookshop.dbtool.data;

import java.util.HashMap;
import java.util.Map;

public class order {
    int id;
    int bookID;
    String userAccount;
    int count;
    boolean isChanged = false;
    public  enum attr{
        id,bookID,userAccount,count,
    }
    public order(int id, int bookID, String userAccount, int count) {
        this.id = id;
        this.bookID = bookID;
        this.userAccount = userAccount;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public int getBookID() {
        return bookID;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public int getCount() {
        return count;
    }
    public Map<String, Object> getDataMap() {
        Map<String, Object> res = new HashMap<>();
        res.put("id", this.id);
        res.put("bookID", this.bookID);
        res.put("userAccouunt", this.userAccount);
        res.put("count", this.count);
        return res;
    }
    public boolean isChanged() {
        return isChanged;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
        isChanged = true;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
        isChanged = true;
    }

    public void setCount(int count) {
        this.count = count;
        isChanged = true;
    }


}