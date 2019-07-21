package com.mlhx.service.bill;

import com.mlhx.dao.BaseDao;
import com.mlhx.dao.bill.BillDao;
import com.mlhx.dao.bill.BillDaoImpl;
import com.mlhx.pojo.Bill;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BillServiceImpl implements BillService {
    private BillDao billDao;
    public BillServiceImpl() {
        billDao=new BillDaoImpl();
    }
    @Override
    //增加订单
    public boolean add(Bill bill) {
        Connection connection=null;
        boolean flag=false;
        try {
            connection=BaseDao.getConnection();
            if(billDao.add(connection,bill)>0)
                flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    //通过条件获取订单列表-模糊查询-billList
    public List<Bill> getBillList(Bill bill) {
        Connection connection=null;
        List<Bill> billList=new ArrayList<Bill>();
        System.out.println("query productName ---- > " + bill.getProductName());
        System.out.println("query providerId ---- > " + bill.getProviderId());
        System.out.println("query isPayment ---- > " + bill.getIsPayment());
        try {
            connection=BaseDao.getConnection();
            billList=billDao.getBillList(connection,bill);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return billList;
    }

    @Override
    //通过billId删除Bill
    public boolean deleteBillById(String delId) {
        Connection connection=null;
        boolean flag=false;
        try {
            connection=BaseDao.getConnection();
            if(billDao.deleteBillById(connection,delId)>0)
                flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    //通过billId获取Bill
    public Bill getBillById(String id) {
        Connection connection=null;
        Bill bill=new Bill();
        try {
            connection=BaseDao.getConnection();
            bill = billDao.getBillById(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
            bill=null;
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return bill;
    }

    @Override
    //修改订单信息
    public boolean modify(Bill bill) {
        Connection connection=null;
        boolean flag= false;
        try {
            connection=BaseDao.getConnection();

            if(billDao.modify(connection,bill)>0)
                flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }
}
