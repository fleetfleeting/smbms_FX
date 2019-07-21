package com.mlhx.dao.role;

import com.mlhx.dao.BaseDao;
import com.mlhx.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        //已经传过来连接
        PreparedStatement pstm=null;
        ResultSet rs=null;
        List<Role> roleList=new ArrayList<Role>();
        if(connection!=null){
            String sql="select * from smbms_role";
            Object[] params={};
            rs=BaseDao.execute(connection,pstm,rs,sql,params);
            //循环查看数据
            while(rs.next()){
                Role _role=new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleCode(rs.getString("roleCode"));
                _role.setRoleName(rs.getString("roleName"));
                roleList.add(_role);
            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return roleList;
    }
}
