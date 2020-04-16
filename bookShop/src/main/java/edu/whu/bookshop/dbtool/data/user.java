package edu.whu.bookshop.dbtool.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class user {
    String account;
    String name;
    String password;
    String address;
    String province;
    String city;
    String info;
    float money;
    private boolean isChanged = false;

    public enum attr {
        account, name, password, address, province, city, info, money,
    }

    user() {
        this.password = "123456";
        this.account = UUID.randomUUID().toString();
        money = 0;
    }

    public user(String account, String name, String password, String address, String province, String city, String info, float money) {
        this.account = account;
        this.name = name;
        this.password = password;
        this.address = address;
        this.province = province;
        this.city = city;
        this.info = info;
        this.money = money;
    }


    public String getAccount() {
        return account;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getInfo() {
        return info;
    }

    public float getMoney() {
        return money;
    }

    public Map<String, Object> getDataMap() {
        Map<String, Object> res = new HashMap<>();
        res.put("account", this.account);
        res.put("password", password);
        res.put("money", money);
        if (this.address != null) res.put("address", this.address);
        if (this.city != null) res.put("city", this.city);
        if (this.province != null) res.put("province", this.province);
        if (this.info != null) res.put("info", this.info);
        return res;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setName(String name) {
        this.name = name;
        isChanged = true;
    }

    public void setPassword(String password) {
        this.password = password;
        isChanged = true;
    }

    public void setAddress(String address) {
        this.address = address;
        isChanged = true;
    }

    public void setProvince(String province) {
        this.province = province;
        isChanged = true;
    }

    public void setCity(String city) {
        this.city = city;
        isChanged = true;
    }

    public void setInfo(String info) {
        this.info = info;
        isChanged = true;
    }

    public void setMoney(float money) {
        this.money = money;
        isChanged = true;
    }


    //    public boolean updata(){
//        if(conn==null)
//            return false;
//        try {
//            PreparedStatement psmt=conn.prepareStatement("UPDATE user SET name=?, password=?, address=?, province=?, city=?, info=?, money=? WHERE account=?");
//            psmt.setString(1,name);
//            psmt.setString(2,password);
//            psmt.setString(3,address);
//            psmt.setString(4,province);
//            psmt.setString(5,city);
//            psmt.setString(6,info);
//            psmt.setFloat(7,money);
//            psmt.setString(8,account);
//            psmt.executeUpdate();
//            psmt.close();
//            return true;
//        }catch (SQLException sqle){
//            System.out.println(sqle);
//            return false;
//        }
//    }

}