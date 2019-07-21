package com.mlhx.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    static{//静态代码块,在类加载的时候执行
        init();
    }

    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    //初始化参数,从配置文件中获得
    public static void init() {
        Properties params = new Properties();
        String configFile = "database.properties";
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream(configFile);
        try {
            params.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver=params.getProperty("driver");
        url=params.getProperty("url");
        user=params.getProperty("user");
        password=params.getProperty("password");
    }

    //获取数据库连接
    public static Connection getConnection(){
        Connection connection=null;
        try {
            Class.forName(driver);
            connection=DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    //查询数据库
    public static ResultSet execute(Connection connection,PreparedStatement pstm,ResultSet rs,String sql,Object[] params ) throws SQLException {
        pstm=connection.prepareStatement(sql);
        for (int i = 0; i <params.length ; i++) {
            pstm.setObject(i+1,params[i]);
        }
        rs=pstm.executeQuery();
        return rs;
    }
    //更新数据库
    public static int execute(Connection connection,PreparedStatement pstm,
                              String sql,Object[] params) throws Exception{
        int updateRows = 0;
        pstm = connection.prepareStatement(sql);
        for(int i = 0; i < params.length; i++){
            pstm.setObject(i+1, params[i]);
        }
        updateRows = pstm.executeUpdate();
        return updateRows;
    }
    //释放资源
    public  static boolean closeResource(Connection connection, PreparedStatement pstm, ResultSet rs){
        boolean flag=true;
        //判断结果集
        if(rs!=null){
            try {
                rs.close();
                rs=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        //判断查询
        if(pstm!=null){
            try {
                pstm.close();
                pstm=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        //判断连接
        if(connection!=null){
            try {
                connection.close();
                connection=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        //返回标识符
        return flag;
    }





}
