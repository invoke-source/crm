package com.szx.crm.workbench.service.Impl;

import com.szx.crm.settings.dao.UserDao;
import com.szx.crm.settings.domain.User;
import com.szx.crm.utils.DateTimeUtil;
import com.szx.crm.utils.UUIDUtil;
import com.szx.crm.workbench.dao.CustomerDao;
import com.szx.crm.workbench.domain.*;
import com.szx.crm.workbench.exception.ActivityClueRelationException;
import com.szx.crm.workbench.exception.ClueConvertException;
import com.szx.crm.workbench.exception.deleteCustomerException;
import com.szx.crm.workbench.service.CustomerService;
import com.szx.crm.workbench.vo.Pagination;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/24 @13:30
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private CustomerDao customerDao;
    @Resource
    private UserDao userDao;
    @Override
    public Pagination<Clue> PageList(Map<String, Object> map) {
        Pagination pagination = new Pagination<Activity>();
        Integer total = customerDao.selectBytotal(map);
        List<Activity> list = customerDao.selectByCustomers(map);
        pagination.setDataList(list);
        pagination.setTotal(total);
        return pagination;
    }

    @Override
    public List<User> getUserList() {
        List<User> list = userDao.getUserList();
        return list;
    }

   @Override
    public Boolean save(Customer customer) {
        Boolean flag = customerDao.save(customer);
        return flag;
    }
    @Override
    public Customer selectById(String id) {
        Customer customer = customerDao.selectById(id);
        return customer;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        Boolean flag = customerDao.updateCustomer(customer);
        return flag;
    }

    @Override
    public Boolean deleteCustomerByid(String[] ids) throws deleteCustomerException {
        Integer count = 0;
        for (String id : ids) {
            Integer c = customerDao.deleteCustomerByid(id);
            count += c;
        }
        if (count != ids.length){
            throw new deleteCustomerException("删除客户信息失败");
        }
        return true;
    }

    @Override
    public Customer detail(String id) {
        Customer customer = customerDao.getCustomerById(id);
        return customer;
    }
    /*
    @Override
    public Clue getClueById(String id) {
        Clue clue = clueDao.getClueById(id);
        return clue;
    }

    @Override
    public List<ClueRemark> getRemarkListByid(String id) {
        List<ClueRemark> list = clueRemarkDao.getRemarkListByid(id);
        return list;
    }

    @Override
    public Boolean saveRemark(ClueRemark clueRemark) {
        Boolean success = clueRemarkDao.saveRemark(clueRemark);
        return success;
    }

    @Override
    public ClueRemark getRemarkByid(String id) {
        ClueRemark clueRemark = clueRemarkDao.getRemarkByid(id);
        return clueRemark;
    }

    @Override
    public Boolean updateRemark(ClueRemark clueRemark) {
        Boolean success = clueRemarkDao.updateRemark(clueRemark);
        return success;
    }

    @Override
    public Boolean deleteRemark(String id) {
        Boolean success = clueRemarkDao.deleteRemark(id);
        return success;
    }



    @Override
    public Boolean deleteRelation(ClueActivityRelation clueActivityRelation) {
        Boolean success = clueActivityRelationDao.deleteRelation(clueActivityRelation);
        return success;
    }

    @Override
    public List<Activity> getActivityRelationListNotByClueId( Map<String,Object> map) {
        List<Activity> list = activityDao.getActivityRelationListNotByClueId(map);
        return list;
    }

    @Override
    public Boolean Relation(List<ClueActivityRelation> list) throws ActivityClueRelationException {
        Integer flag = 0;
        for (ClueActivityRelation clueActivityRelation : list) {
            Boolean success = clueActivityRelationDao.Relation(clueActivityRelation);
            if (success){
                flag++;
            }
        }
        if (list.size() != flag) {
            throw new ActivityClueRelationException("市场活动关联失败");
        }
        return true;
    }
*/
}
