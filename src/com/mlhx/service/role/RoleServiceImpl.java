package com.mlhx.service.role;


import com.mlhx.dao.BaseDao;
import com.mlhx.dao.role.RoleDao;
import com.mlhx.dao.role.RoleDaoImpl;
import com.mlhx.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService{
	private RoleDao roleDao;

	public RoleServiceImpl() {
		roleDao=new RoleDaoImpl();
	}

	@Override
	public List<Role> getRoleList() {
		Connection connection=null;
		List<Role> roleList=new ArrayList<Role>();
		try {
			connection=BaseDao.getConnection();
			roleList=roleDao.getRoleList(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			BaseDao.closeResource(connection,null,null);
		}
		return roleList;
	}
}
