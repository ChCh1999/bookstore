package edu.whu.dbtool;

import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

public class BaseDataUtil {
    private Connection conn = null;
    private final int batchSize = 100;

    public BaseDataUtil() {
        try {
            //加载驱动
//            Class.forName("com.mysql.cj.jdbc.Driver");
            //getConnecting（）方法，用来连接mysql的数据库
//            conn = DriverManager.getConnection("jdbc:mysql://" + dbUrl + "?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true  ", dbUser, dbPW);
            conn=DBUtil.getConnection();
            if (!conn.isClosed()) {
                System.out.println("Succeeded connecting to the Database");
            }
            conn.close();
        }  catch (SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace(); //handle other exception
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (conn != null) {
            conn.commit();
            conn.close();
        }
    }

    private void getConnection() throws SQLException {
        this.conn.close();
        this.conn=DBUtil.getConnection();
    }

    /**
     * SQL更新语句执行
     * @param sql 语句模板串
     * @param keyList 关键字
     * @param params 参数map
     * @return 更新行数
     * @throws DBException 参数不完整或SQL语句执行过程中出错
     */
    private int executeUpdate(String sql, List<String> keyList, Map<String, Object> params) throws DBException {
        try {
            //设置参数
            getConnection();
            PreparedStatement psmt = conn.prepareStatement(sql.toString());
            int len = keyList.size();
            for (int i = 0; i < len; i++) {
                psmt.setObject(i + 1, params.get(keyList.get(i)));
            }
            int res=psmt.executeUpdate();
            return res;

        } catch (SQLException sqle) {
            //sqle.printStackTrace();
            throw new DBException(sqle.getMessage());
        }
    }

    private int executeUpdateBatch(String sql, List<String> keyList, List<Map<String, Object>> batchsData) throws DBException {
        try {
            //设置参数，添加batch
            int updataCount = 0;
            int count = 0;
            getConnection();
            PreparedStatement psmt = conn.prepareStatement(sql);

            int totalCount = batchsData.size();
            for (Map<String, Object> params : batchsData) {
                int keyCount = keyList.size();
                if (params.size() != keyCount) continue;
                boolean wrongData = false;
                for (int j = 0; j < keyCount; j++) {
                    if (params.get(keyList.get(j)) == null) {
                        wrongData = true;
                        break;
                    }
                    psmt.setObject(j + 1, params.get(keyList.get(j)));
                }
                if (wrongData) continue;
                psmt.addBatch();

                if (++count % batchSize == 0) {
                    for (int rows : psmt.executeBatch()) {
                        updataCount += rows;
                    }
                }
            }
            for (int rows : psmt.executeBatch()) {
                updataCount += rows;
            }
            return updataCount;

        } catch (SQLException sqle) {
            //sqle.printStackTrace();
            throw new DBException(sqle.getMessage());
        }


    }

    /**
     * 按照参数构造插入语句
     * @param table 表名
     * @param params 参数
     * @param keyList 用于存储键值列表，与SQL模板中？一一对应
     * @return SQL模板字符串
     * @throws DBException 参数为空抛出错误
     */
    private String buildInsertSQL(String table, Map<String, Object> params, List<String> keyList) throws DBException {
        //构造SQL语句
        StringBuilder sql = new StringBuilder("INSERT INTO "  + "`"+ table+"` ");
        StringBuilder valueString = new StringBuilder("VALUE (");
        keyList.clear(); //参数对照表
        if (params != null && params.size() > 0) {
            sql.append("(");
            Iterator<Entry<String, Object>> entries = params.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> entry = entries.next();
                sql.append(entry.getKey());
                valueString.append("?");
                keyList.add(entry.getKey());
                if (entries.hasNext()) {
                    sql.append(",");
                    valueString.append(",");
                } else {
                    sql.append(")");
                    valueString.append(");");
                }
            }
        } else throw new DBException("INSERT Wrong Data");

        sql.append(valueString.toString());
        return sql.toString();
    }

    /**
     * 插入数据
     *
     * @param table  表名
     * @param params 参数映射
     * @return 是否插入成功
     * @throws DBException 参数有误或者执行出错
     */
    public int insertData(String table, Map<String, Object> params) throws DBException {
        List<String> keyList = new ArrayList<>();
        String sql = buildInsertSQL(table, params, keyList);
        return executeUpdate(sql, keyList, params);
    }

    public int insertDataBatch(String table, List<Map<String, Object>> paramsList) throws DBException {
        List<String> keyList = new ArrayList<>();
        String sql = buildInsertSQL(table, paramsList.get(0), keyList);
        return executeUpdateBatch(sql, keyList, paramsList);
    }

    /**
     * 删除数据
     *
     * @param table  表名
     * @param params 参数列表
     * @return 更改行数
     * @throws DBException
     */
    public int deleteData(String table, Map<String, Object> params) throws DBException {
        //构造SQL语句
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM "  + "`"+ table+"` ");
        List<String> keyList = new ArrayList<>();

        if (params != null && params.size() > 0) {
            sqlBuilder.append(" WHERE ");
            Iterator<Entry<String, Object>> entries = params.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> entry = entries.next();
                sqlBuilder.append(entry.getKey());
                keyList.add(entry.getKey());
                if (entries.hasNext()) sqlBuilder.append("=? AND ");
                else sqlBuilder.append("=?");
            }
        } else throw new DBException("DELETE call with wrong data");

        return executeUpdate(sqlBuilder.toString(), keyList, params);
    }


    /**
     * 删除指定表的全部数据
     *
     * @param table 表名
     * @throws DBException
     */
    public void deleteALL(String table) throws DBException {
        try {
            getConnection();
            PreparedStatement pstm = conn.prepareStatement("DELETE FROM "  + "`"+ table+"` ");
            pstm.executeUpdate();
        } catch (SQLException sqle) {
            //sqle.printStackTrace();
            throw new DBException(sqle.getMessage());
        }
    }


    private String buildUpdataSQL(String table, Map<String, Object> params, Map<String, Object> conditions, List<String> keyList) throws DBException {
        //构造SQL语句
        StringBuilder sql = new StringBuilder("UPDATE "  + "`"+ table+"` ");
        StringBuilder valueString = new StringBuilder(" SET ");
        StringBuilder conString = new StringBuilder(" WHERE ");
        keyList.clear();
        if (params != null && params.size() > 0) {
            if (conditions != null && conditions.size() > 0) {

                Iterator<Entry<String, Object>> paramEntries = params.entrySet().iterator();
                while (paramEntries.hasNext()) {
                    Entry<String, Object> entry = paramEntries.next();
                    if (conditions.containsKey(entry.getKey())) continue;
                    valueString.append(entry.getKey());
                    valueString.append("=?");
                    keyList.add(entry.getKey());
                    if (paramEntries.hasNext()) {
                        valueString.append(",");
                    } else {
                        valueString.append(" ");
                    }
                }
                valueString.deleteCharAt(valueString.toString().length() - 1);

                Iterator<Entry<String, Object>> conEntries = conditions.entrySet().iterator();
                while (conEntries.hasNext()) {
                    Entry<String, Object> entry = conEntries.next();
                    conString.append(entry.getKey());
                    conString.append("=?");
                    keyList.add(entry.getKey());
                    if (conEntries.hasNext()) {
                        conString.append(",");
                    } else {
                        conString.append(";");
                    }
                }


            } else throw new DBException("UPDATA with wrong condition");
        } else throw new DBException("UPDATA Wrong Data");

        sql.append(valueString.toString());
        sql.append(conString.toString());
        return sql.toString();
    }

    /**
     * 按照指定条件更新数据
     *
     * @param table      表名
     * @param params     更新参数表
     * @param conditions 条件参数表
     * @return 修改的行数
     * @throws DBException
     */
    public int updateData(String table, Map<String, Object> params, Map<String, Object> conditions) throws DBException {
        List<String> keyList = new ArrayList<>();
        String sql = buildUpdataSQL(table, params, conditions, keyList);
        Map<String, Object> par = new HashMap<>();
        par.putAll(params);
        par.putAll(conditions);
        return executeUpdate(sql, keyList, par);
    }

    /**
     * 更新一组数据
     *
     * @param table          数据表
     * @param paramsList     参数
     * @param conditionsList 条件
     * @return 成功修改的行数
     * @throws DBException
     */
    public int updateDataBatch(String table, List<Map<String, Object>> paramsList, List<Map<String, Object>> conditionsList) throws DBException {
        try {
            //构造SQL语句

            List<String> keyList = new ArrayList<>();   //参数对照表
            Map<String, Object> paramTemp = paramsList.get(0);
            Map<String, Object> conditionTemp = conditionsList.get(0);

            String sql = buildUpdataSQL(table, paramTemp, conditionTemp, keyList);

            int totalCount = Math.min(paramsList.size(), conditionsList.size());
            List<Map<String, Object>> par = new ArrayList<>();
            for (int i = 0; i < totalCount; i++) {
                Map<String, Object> params = new HashMap<>();
                params.putAll(paramsList.get(i));
                params.putAll(conditionsList.get(i));
                par.add(params);
            }
            return executeUpdateBatch(sql, keyList, par);

        } catch (SQLException sqle) {
            //sqle.printStackTrace();
            throw new DBException(sqle.getMessage());
        }
    }

    /**
     * 按照指定条件更行参数表
     *
     * @param table  表名
     * @param params 参数表
     * @return 查询结果集合
     * @throws DBException
     */
    public ResultSet searchData(String table, Map<String, Object> params) throws DBException {
        try {
            //构造SQL语句
            StringBuilder sql = new StringBuilder("SELECT * FROM " + "`"+ table+"` ");
            List<Object> paramsValList = new ArrayList<>();

            if (params != null && params.size() > 0) {
                sql.append(" WHERE ");
                Iterator<Entry<String, Object>> entries = params.entrySet().iterator();
                while (entries.hasNext()) {
                    Entry<String, Object> entry = entries.next();
                    sql.append(entry.getKey());
                    paramsValList.add(entry.getValue());
                    if (entries.hasNext()) {
                        sql.append("=? AND ");
                    } else {
                        sql.append("=?");
                    }
                }
            }

            //设置参数
            getConnection();
            PreparedStatement psmt = conn.prepareStatement(sql.toString());
            int len = paramsValList.size();
            for (int i = 0; i < len; i++) {
                psmt.setObject(i + 1, paramsValList.get(i));
            }

            //返回查询结果
            ResultSet res=psmt.executeQuery();
            return res;

        } catch (SQLException sqle) {
//            //sqle.printStackTrace();
            throw new DBException(sqle.getMessage());
        }
    }

}
