package org.DB;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbOperation {

//    public static String url = "jdbc:mysql://172.16.200.87:3306/nemp";
//    public static String userName = "root";
//    public static String passWord = "CeShi#0301!";

    /**
     * 链接数据库
     *
     * @return 连接状态
     */
    public static Connection connection(String url,String userName,String passWord) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("驱动加载成功！ Add driver success!");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(url, userName, passWord);
            System.out.println("数据库连接成功！ Connection success!");
            return conn;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放连接
     *
     * @param rs
     * @param st
     * @param conn
     */
    private static void free(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close(); // 关闭结果集
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close(); // 关闭Statement
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close(); // 关闭连接
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    /**
     * 查询语句，把结果集存入list中
     *
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> MySqlSelect(String url,String userName,String passWord,String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbOperation.connection(url, userName, passWord);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = ps.getMetaData();

            // 取得结果集列数
            int columnCount = rsmd.getColumnCount();

            // 构造泛型结果集
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            Map<String, Object> data = null;

            // 循环结果集
            //System.out.println(rs.next());
            while (rs.next()) {
                data = new HashMap<String, Object>();
                // 每循环一条将列名和列值存入Map
                for (int i = 1; i < columnCount; i++) {
                    try {
                        data.put(rsmd.getColumnLabel(i), rs.getObject(rsmd
                                .getColumnLabel(i)));
                    } catch (Exception e) {
                        //	System.out.println("数据存储："+rsmd.getColumnLabel(i));
                        e.printStackTrace();
                    }
                }
                // 将整条数据的Map存入到List中
                dataList.add(data);
            }
            //  System.out.println("准备释放");
            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            // System.out.println("执行finally");
            DbOperation.free(rs, ps, conn);
            //  System.out.println("释放成功");
        }
    }

    /**
     * 插入操作
     *
     * @param sql
     */
    public static void MysqlInsert(String url,String userName,String passWord,String sql) {
        Connection conn = null;
        PreparedStatement pst = null;
        //ResultSet rs = null;
        int lines=0;
        try {
            conn = DbOperation.connection(url, userName, passWord);
            pst = conn.prepareStatement(sql);
            lines = pst.executeUpdate();
            System.out.println(
                       "插入"+lines+"行。插入成功！");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("插入"+lines+"行。插入失败！");
        }finally {
            DbOperation.free(null, pst, conn);
        }


    }

    /**
     * 更新操作
     *
     * @param sql
     */
    public static void MysqlUpdate(String url,String userName,String passWord,String sql) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int lines=0;
        try {
            conn = DbOperation.connection(url, userName, passWord);
            pst = conn.prepareStatement(sql);
            lines  = pst.executeUpdate();
            if(lines>0){
                System.out.println("更新成功！更新"+lines+"条数据");
            }else {
                System.out.println("未更新！更新0条数据");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("更新失败！");
        } finally {
            DbOperation.free(null, pst, conn);
        }


    }

    /**
     * 删除操作
     *
     * @param sql
     */
    public static void MysqlDelete(String url,String userName,String passWord,String sql) {

        Connection conn = null;
        PreparedStatement pst = null;
        //ResultSet rs = null;
        int lines=0;
        try {
            conn = DbOperation.connection(url, userName, passWord);
            pst = conn.prepareStatement(sql);
            lines = pst.executeUpdate();

            System.out.println("删除成功！删除了"+lines+"条数据");
        } catch (SQLException throwable) {
            throwable.getStackTrace();
            System.out.println("删除失败！");
        } finally {
            DbOperation.free(null, pst, conn);
        }
    }


}
