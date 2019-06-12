package com.mime.concurrent.DistributionMySqlReentrantLock;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author zhangjiaheng
 * @Description jdbc连接 sql执行器
 **/
public class JdbcExecutor {

    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    public static Connection getCon() {
        Connection connection = connectionThreadLocal.get();
        if(null != connection){
            return connection;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.
                    getConnection("jdbc:mysql://localhost:3306/base?useUnicode=true&characterEncoding=UTF-8",
                            "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectionThreadLocal.set(connection);
        return connection;
    }

    /**
     * 加锁 默认过期时间为5秒
     */
    public static boolean insertLock(String methodWholeName, String uuid){
        return insertLock(methodWholeName, uuid, 5);
    }

    public static boolean insertLock(String methodWholeName, String uuid, int seconds) {
        try {
            Date date = new Date();
            Date next = rollSec(date, seconds);
            String sql = "insert into mysql_lock(id, uuid, expireTime, methodWholeName) values(?, ?, ?, ?)";
            PreparedStatement ps = getCon().prepareCall(sql);
            ps.setInt(1, 1);
            ps.setString(2, uuid);
            ps.setObject(3, next);
            ps.setString(4, methodWholeName);
            ps.execute();
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    public static void delete(String methodWholeName, String uuid) throws SQLException {
        String sql = "delete from mysql_lock where uuid = ? and methodWholeName = ?";
        final Connection connection = getCon();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, uuid);
        ps.setString(2, methodWholeName);
        ps.execute();
    }

    public static boolean select(String methodWholeName, String uuid) throws SQLException {
        Date date = new Date();
        String selectSql = "select count(1) from mysql_lock where uuid = ? and methodWholeName = ? and expireTime > ?";
        String deleteSql = "delete from mysql_lock where expireTime <= ?";
        final Connection connn = getCon();
        connn.setAutoCommit(false);
        boolean flag = false;
        PreparedStatement delPs = connn.prepareStatement(deleteSql);
        // 清理掉过期的锁
        delPs.setObject(1, date);
        delPs.execute();
        PreparedStatement ps = connn.prepareStatement(selectSql);
        ps.setString(1, uuid);
        ps.setString(2, methodWholeName);
        ps.setObject(3, date);

        final ResultSet b = ps.executeQuery();
        if (b.next()) {
            if (b.getInt(1) > 0) {
                flag = true;
            }
        }
        connn.commit();
        return flag;
    }

    public static Date rollSec(Date d, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.SECOND, sec);
        return cal.getTime();
    }
}
