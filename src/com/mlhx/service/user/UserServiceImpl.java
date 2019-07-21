package com.mlhx.service.user;

import com.mlhx.dao.BaseDao;
import com.mlhx.dao.user.UserDao;
import com.mlhx.dao.user.UserDaoImpl;
import com.mlhx.pojo.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
/**
 * service层捕获异常，进行事务处理
 * 事务处理：调用不同dao的多个方法，必须使用同一个connection（connection作为参数传递）
 * 事务完成之后，需要在service层进行connection的关闭，在dao层关闭（PreparedStatement和ResultSet对象）
 *
 */

public class UserServiceImpl implements UserService{
    private UserDao userDao;
    public  UserServiceImpl(){
        userDao=new UserDaoImpl();
    }
    @Override
    //增加用户信息
    public boolean add(User user) {
        boolean flag=false;
        Connection connection=null;
        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务管理
            int updateRows = userDao.add(connection, user);
            connection.commit();//事务
            if(updateRows>0){
                flag=true;
                System.out.println("添加用户成功");
            }else{
                System.out.println("添加用户失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally{
            //在service层关闭connection的连接
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    //用户登录
    public User login(String userCode, String userPassword) {
        Connection connection=null;
        User user=new User();
        try {
            connection=BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //关闭connection连接
            BaseDao.closeResource(connection,null,null);
        }
        //匹配密码
        if(user!=null){
            if(!user.getUserPassword().equals(userPassword)){
                user=null;
            }
        }
        return user;
    }

    @Override
    //获得用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection=null;
        List<User> userList=null;
        System.out.println("queryUserName ---- > " + queryUserName);
        System.out.println("queryUserRole ---- > " + queryUserRole);
        System.out.println("currentPageNo ---- > " + currentPageNo);
        System.out.println("pageSize ---- > " + pageSize);
        try {
            connection=BaseDao.getConnection();
            userList=userDao.getUserList(connection,queryUserName,queryUserRole,currentPageNo,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection,null,null);
        }
        return userList;
    }

    @Override
    //根据条件查询用户表记录数
    public int getUserCount(String queryUserName, int queryUserRole) {
        Connection connection=null;
        int count=0;
        System.out.println("queryUserName ---- > " + queryUserName);
        System.out.println("queryUserRole ---- > " + queryUserRole);

        try {
            connection=BaseDao.getConnection();
            count = userDao.getUserCount(connection, queryUserName, queryUserRole);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return count;
    }

    @Override
    //根据userCode查询出User
    public User selectUserCodeExist(String userCode) {
        Connection connection=null;
        User user =new User();
        try {
            connection=BaseDao.getConnection();
            user=userDao.getLoginUser(connection,userCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    @Override
    //根据id删除用户
    public boolean deleteUserById(Integer delId) {
        Connection connection=null;
        boolean flag=false;
        try {
            connection=BaseDao.getConnection();
            if(userDao.deleteUserById(connection,delId)>0){
                flag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    //根据id获得用户
    public User getUserById(String id) {
        Connection connection=null;
        User user =new User();

        try {
            connection=BaseDao.getConnection();
            user=userDao.getUserById(connection,id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    @Override
    //修改用户信息
    public boolean modify(User user) {
        Connection connection=null;
        boolean flag=false;
        connection=BaseDao.getConnection();
        try {
            if(userDao.modify(connection, user)>0){
                flag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    //更新密码
    public boolean updatePwd(int id, String pwd) {
        Connection connection=null;
        boolean flag=false;

        try {
            connection=BaseDao.getConnection();
            if(userDao.updatePwd(connection, id, pwd)>0)
                flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }
}
