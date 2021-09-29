package com.szx.crm.workbench.service.Impl;

import com.szx.crm.utils.DateTimeUtil;
import com.szx.crm.utils.UUIDUtil;
import com.szx.crm.workbench.dao.CustomerDao;
import com.szx.crm.workbench.dao.TranDao;
import com.szx.crm.workbench.dao.TranHistoryDao;
import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.Customer;
import com.szx.crm.workbench.domain.Tran;
import com.szx.crm.workbench.domain.TranHistory;
import com.szx.crm.workbench.exception.ChangeStageExceeption;
import com.szx.crm.workbench.exception.TranSaveException;
import com.szx.crm.workbench.service.TranService;
import com.szx.crm.workbench.vo.Pagination;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @2021/9/26 @14:03
 */
@Service
public class TranServiceImpl implements TranService {
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;
    @Resource
    private CustomerDao customerDao;

    @Override
    //保存交易表记录
    public void savaTran(Tran tran, String customerName) throws TranSaveException {
        Customer customer = customerDao.getCustomerByName(customerName);
        //当客户不存在时，需要新建客户
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setOwner(tran.getOwner());
            customer.setName(customerName);
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setDescription(tran.getDescription());
            Boolean flag = customerDao.save(customer);
            if (!flag){
                throw new TranSaveException("客户信息保存失败");
            }
        }
        //保存交易信息
        tran.setCustomerId(customer.getId());
        Boolean flag = tranDao.save(tran);
        if (!flag){
            throw new TranSaveException("交易信息保存失败");
        }
        //保存交易信息历史
        TranHistory tranHistory = new TranHistory();

        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setTranId(tran.getId());
        Boolean flag1 = tranHistoryDao.save(tranHistory);
        if (!flag1){
            throw new TranSaveException("交易信息历史保存失败");
        }

    }

    @Override
    public Pagination PageList(Map<String,Object> map) {

        Pagination pagination = new Pagination<Activity>();
        Integer total = tranDao.selectBcytotal(map);
        List<Activity> list = tranDao.selectByActivitys(map);
        pagination.setDataList(list);
        pagination.setTotal(total);
        return pagination;
    }

    //根据id取Tran
    @Override
    public Tran getTranById(String id) {
        Tran tran = tranDao.getTranById(id);
        return tran;
    }

    @Override
    public List<TranHistory> showHistoryList(String tranId,Map<String,String> map) {
        List<TranHistory> list = tranHistoryDao.getHistoryListByTranId(tranId);
        for (TranHistory tranHistory : list) {
            String possibility =  map.get(tranHistory.getStage());
            tranHistory.setPossibility(possibility);
        }
        return list;
    }

    @Override
    public Boolean changeStage(String tranid, TranHistory tranHistory) throws ChangeStageExceeption {
        String stage = tranHistory.getStage();
        Tran tran = new Tran();
        tran.setId(tranid);
        tran.setStage(stage);
        tran.setEditBy(tranHistory.getCreateBy());
        tran.setEditTime(DateTimeUtil.getSysTime());
        boolean flag = tranDao.changeStage(tran);
        boolean flag1 = tranHistoryDao.save(tranHistory);
        if (flag != flag1){
            throw  new ChangeStageExceeption("x修改阶段信息失败");
        }
        return true;
    }


}
