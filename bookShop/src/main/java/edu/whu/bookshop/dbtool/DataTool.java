package edu.whu.bookshop.dbtool;


import edu.whu.bookshop.dbtool.data.book;
import edu.whu.bookshop.dbtool.data.order;
import edu.whu.bookshop.dbtool.data.user;
import edu.whu.mSpring.annotation.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataTool {
    private BaseDataUtil dbUtil = new BaseDataUtil();

    private List<user> rs2UserList(ResultSet rs) {
        List<user> res = new ArrayList<>();
        try {
            while (rs.next()) {
                user temp = new user(
                        rs.getString("account"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("province"),
                        rs.getString("city"),
                        rs.getString("address"),
                        rs.getString("info"),
                        rs.getFloat("money")
                );
                res.add(temp);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return res;
    }

    private List<book> rs2BookList(ResultSet rs) {
        List<book> res = new ArrayList<>();
        try {
            while (rs.next()) {
                book temp = new book(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("publisher"),
                        rs.getString("img"),
                        rs.getString("info"),
                        rs.getInt("storeCount")
                );
                res.add(temp);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return res;
    }

    private List<order> rs2OrderList(ResultSet rs) {
        List<order> res = new ArrayList<>();
        try {
            while (rs.next()) {
                order temp = new order(
                        rs.getInt("id"),
                        rs.getInt("bookID"),
                        rs.getString("userAccount"),
                        rs.getInt("count")
                );
                res.add(temp);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
        return res;
    }

    //book

    /**
     * 批量插入图书
     *
     * @param books 图书列表
     * @return 全部成功插入 true;否则 false;
     */
    public boolean insertBook(List<book> books) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (book temp : books) data.add(temp.getDataMap());
        try {
            int updataCount = dbUtil.insertDataBatch("book", data);
            return updataCount == books.size();
        } catch (DBException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 插入单本图书
     *
     * @param aBook 图书对象
     * @return 是否插入成功
     */
    public boolean insertBook(book aBook) {
        try {
            return dbUtil.insertData("book", aBook.getDataMap()) == 1;
        } catch (DBException dbe) {
            System.out.println(dbe.getMessage());
            return false;
        }
    }

    /**
     * 更新图书，条件为ID
     *
     * @param aBook
     * @return
     */
    public boolean updateBook(book aBook) {
//        if(!aBook.isChanged())return true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", aBook.getId());
            return dbUtil.updateData("book", aBook.getDataMap(), map) == 1;
        } catch (DBException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量更新列表中的book id对应的数据项
     *
     * @param books
     * @return 更新的行数
     */
    public int updateBook(List<book> books) {
        List<Map<String, Object>> data = new ArrayList<>();
        List<Map<String, Object>> conList = new ArrayList<>();
        for (book temp : books) {
            if (!temp.isChanged()) continue;
            data.add(temp.getDataMap());
            Map<String, Object> map = new HashMap<>();
            map.put("id", temp.getId());
            conList.add(map);
        }
        try {
            return dbUtil.updateDataBatch("book", data, conList);
        } catch (DBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据ID删除图书
     *
     * @param id 图书ID
     * @return 成功返回更改行数；否则返回-1
     */
    public int deleteBookByID(int id) {
        Map<String, Object> con = new HashMap<>();
        con.put("id", id);
        try {
            return (dbUtil.deleteData("book", con));
        } catch (DBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 自定义删除图书
     *
     * @param condition 条件字典
     * @return
     */
    public int deleteBook(Map<String, Object> condition) {
        try {
            //不允许直接删除全部
            if (condition == null || condition.size() == 0)
                return -1;
            return dbUtil.deleteData("book", condition);
        } catch (DBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 使用名字查找图书
     *
     * @param name
     * @return
     */
    public List<book> searchBookByName(String name) {
        Map<String, Object> con = new HashMap<>();
        con.put("name", name);
        try {
            ResultSet rs = dbUtil.searchData("book", con);
            return rs2BookList(rs);
        } catch (DBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 其他图书查询
     *
     * @param con map<属性，值>
     * @return 返回结果book列表，出错则为null
     */
    public List<book> searchBook(Map<String, Object> con) {
        try {
            return rs2BookList(dbUtil.searchData("book", con));
        } catch (DBException e) {
            e.printStackTrace();
            return null;
        }
    }

    //order

    /**
     * 批量插入订单
     *
     * @param orders 订单列表
     * @return 全部成功插入 true;否则 false;
     */
    public boolean insertOrder(List<order> orders) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (order temp : orders) data.add(temp.getDataMap());
        try {
            int updataCount = dbUtil.insertDataBatch("order", data);
            return updataCount == orders.size();
        } catch (DBException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 插入单个订单
     *
     * @param aOrder 订单对象
     * @return 是否插入成功
     */
    public boolean insertOrder(order aOrder) {
        try {
            return dbUtil.insertData("order", aOrder.getDataMap()) == 1;
        } catch (DBException dbe) {
            System.out.println(dbe.getMessage());
            return false;
        }
    }

    /**
     * 更新订单，条件为ID
     *
     * @param aOrder
     * @return
     */
    public boolean updateOrder(order aOrder) {
//        if(!aOrder.isChanged())return true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", aOrder.getId());
            return dbUtil.updateData("order", aOrder.getDataMap(), map) == 1;
        } catch (DBException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量更新列表中的order id对应的数据项
     *
     * @param orders
     * @return 更新的行数
     */
    public int updateOrder(List<order> orders) {
        List<Map<String, Object>> data = new ArrayList<>();
        List<Map<String, Object>> conList = new ArrayList<>();
        for (order temp : orders) {
            if (!temp.isChanged()) continue;
            data.add(temp.getDataMap());
            Map<String, Object> map = new HashMap<>();
            map.put("id", temp.getId());
            conList.add(map);
        }
        try {
            return dbUtil.updateDataBatch("order", data, conList);
        } catch (DBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据ID删除订单
     *
     * @param id 订单ID
     * @return 成功返回更改行数；否则返回-1
     */
    public int deleteOrderByID(int id) {
        Map<String, Object> con = new HashMap<>();
        con.put("id", id);
        try {
            return (dbUtil.deleteData("order", con));
        } catch (DBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 自定义删除订单
     *
     * @param condition 条件字典
     * @return 更改的数据项
     */
    public int deleteOrder(Map<String, Object> condition) {
        try {
            //不允许直接删除全部
            if (condition == null || condition.size() == 0)
                return -1;
            return dbUtil.deleteData("order", condition);
        } catch (DBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 使用图书ID查找订单
     *
     * @param bookID
     * @return 查询结构，出错返回null
     */
    public List<order> searchOrderByBookID(int bookID) {
        Map<String, Object> con = new HashMap<>();
        con.put("bookID", bookID);
        try {
            ResultSet rs = dbUtil.searchData("order", con);
            return rs2OrderList(rs);
        } catch (DBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 其他订单查询
     *
     * @param con map<属性，值>
     * @return 返回结果order列表，出错则为null
     */
    public List<order> searchOrder(Map<String, Object> con) {
        try {
            return rs2OrderList(dbUtil.searchData("order", con));
        } catch (DBException e) {
            e.printStackTrace();
            return null;
        }
    }


    //user

    /**
     * 批量插入用户
     *
     * @param users 用户列表
     * @return 全部成功插入 true;否则 false;
     */
    public boolean insertUser(List<user> users) {
        List<Map<String, Object>> data = new ArrayList<>();
        for (user temp : users) data.add(temp.getDataMap());
        try {
            int updataCount = dbUtil.insertDataBatch("user", data);
            return updataCount == users.size();
        } catch (DBException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 插入单个用户
     *
     * @param aUser 用户对象
     * @return 是否插入成功
     */
    public boolean insertUser(user aUser) {
        try {
            return dbUtil.insertData("user", aUser.getDataMap()) == 1;
        } catch (DBException dbe) {
            System.out.println(dbe.getMessage());
            return false;
        }
    }

    /**
     * 更新用户各个属性，条件为账户
     *
     * @param aUser
     * @return
     */
    public boolean updateUser(user aUser) {
//        if(!aUser.isChanged())return true;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", aUser.getAccount());
            return dbUtil.updateData("user", aUser.getDataMap(), map) == 1;
        } catch (DBException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量更新列表中的user account对应的数据项
     *
     * @param users
     * @return 更新的行数
     */
    public int updateUser(List<user> users) {
        List<Map<String, Object>> data = new ArrayList<>();
        List<Map<String, Object>> conList = new ArrayList<>();
        for (user temp : users) {
            if (!temp.isChanged()) continue;
            data.add(temp.getDataMap());
            Map<String, Object> map = new HashMap<>();
            map.put("account", temp.getAccount());
            conList.add(map);
        }
        try {
            return dbUtil.updateDataBatch("user", data, conList);
        } catch (DBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据account删除用户
     *
     * @param account 用户账号
     * @return 成功返回更改行数；否则返回-1
     */
    public int deleteUserByID(int account) {
        Map<String, Object> con = new HashMap<>();
        con.put("id", account);
        try {
            return (dbUtil.deleteData("user", con));
        } catch (DBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 自定义删除用户
     *
     * @param condition 条件字典
     * @return 更改的数据项
     */
    public int deleteUser(Map<String, Object> condition) {
        try {
            //不允许直接删除全部
            if (condition == null || condition.size() == 0)
                return -1;
            return dbUtil.deleteData("user", condition);
        } catch (DBException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 使用名字查找用户
     *
     * @param name
     * @return 查询结构，出错返回null
     */
    public List<user> searchUserByBookID(int name) {
        Map<String, Object> con = new HashMap<>();
        con.put("name", name);
        try {
            ResultSet rs = dbUtil.searchData("user", con);
            return rs2UserList(rs);
        } catch (DBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 其他用户查询
     *
     * @param con map<属性，值>
     * @return 返回结果user列表，出错则为null
     */
    public List<user> searchUser(Map<String, Object> con) {
        try {
            return rs2UserList(dbUtil.searchData("user", con));
        } catch (DBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
