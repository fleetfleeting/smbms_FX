package com.mlhx.service.provider;

import com.mlhx.dao.BaseDao;
import com.mlhx.dao.bill.BillDao;
import com.mlhx.dao.bill.BillDaoImpl;
import com.mlhx.dao.provider.ProviderDao;
import com.mlhx.dao.provider.ProviderDaoImpl;
import com.mlhx.pojo.Provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
 * 若订单表中无该供应商的订单数据，则可以删除
 * 若有该供应商的订单数据，则不可以删除
 * 返回值billCount
 * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
 * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
 *
 * ---判断
 * 如果billCount = -1 失败
 * 若billCount >= 0 成功
 */


public class ProviderServiceImpl implements  ProviderService {
    private ProviderDao providerDao;
    private BillDao billDao;
    public ProviderServiceImpl(){
        providerDao=new ProviderDaoImpl();
        billDao=new BillDaoImpl();
    }
    @Override
    //增加供应商
    public boolean add(Provider provider) {
        Connection connection=null;
        boolean flag=false;

        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);//开启事务
            if(providerDao.add(connection,provider)>0){
                flag=true;
                connection.commit();//提交事务
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                System.out.println("rollback=============");
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }finally {
                BaseDao.closeResource(connection,null,null);
            }
        }
        return flag;
    }

    @Override
    //通过供应商名称、编码获取供应商列表-模糊查询-providerList
    public List<Provider> getProviderList(String proName, String proCode) {
        Connection connection=null;
        List<Provider> providerList=new ArrayList<Provider>();
        try {
            connection=BaseDao.getConnection();
            providerList= providerDao.getProviderList(connection, proName, proCode);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return providerList;
    }

    @Override
    //通过proId删除Provider
    public int deleteProviderById(String delId) {
        Connection connection=null;
        int billCount=-1;
        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);
            billCount=billDao.getBillCountByProviderId(connection,delId);
            if(billCount==0){
                providerDao.deleteProviderById(connection,delId);
            }
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
            billCount=-1;
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return billCount;
    }

    @Override
    //通过proId获取Provider
    public Provider getProviderById(String id) {
        Connection connection=null;
        Provider provider=new Provider();
        try {
            connection=BaseDao.getConnection();
            provider = providerDao.getProviderById(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
            provider=null;
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return provider;
    }

    @Override
    //修改供应商信息
    public boolean modify(Provider provider) {
        Connection connection=null;
        boolean flag=false;
        try {
            connection=BaseDao.getConnection();
            if(providerDao.modify(connection, provider)>0)
                flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }
}
