package com.szx.crm.workbench.service.Impl;

import com.szx.crm.utils.DateTimeUtil;
import com.szx.crm.utils.UUIDUtil;
import com.szx.crm.workbench.dao.*;
import com.szx.crm.workbench.domain.*;
import com.szx.crm.workbench.exception.ActivityClueRelationException;
import com.szx.crm.workbench.exception.ClueConvertException;
import com.szx.crm.workbench.service.ClueService;
import com.szx.crm.workbench.vo.Pagination;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/16 @14:34
 */
@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private ActivityDao activityDao;
    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueRemarkDao clueRemarkDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;
    //客户相关
    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerRemarkDao customerRemarkDao;
    //联系人相关
    @Resource
    private ContactsDao contactsDao;
    @Resource
    private ContactsRemarkDao contactsRemarkDao;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao;
    //交易相关
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

    @Override
    public Pagination<Clue> PageList(Map<String, Object> map) {
        Pagination pagination = new Pagination<Activity>();
        Integer total = clueDao.selectBytotal(map);
        List<Activity> list = clueDao.selectByActivitys(map);
        pagination.setDataList(list);
        pagination.setTotal(total);
        return pagination;
    }

    @Override
    public Boolean save(Clue clue) {
        Boolean flag = clueDao.save(clue);
        return flag;
    }

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

    @Override
    public void convert(String id , Tran tran) throws ClueConvertException {
        //获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue =clueDao.getById(id);
        Boolean flag = true;
        //通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        Customer customer = customerDao.getCustomerByName(clue.getCompany());
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(customer.getAddress());
            customer.setOwner(clue.getOwner());
            customer.setName(clue.getCompany());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(clue.getCreateBy());
            customer.setCreateTime(clue.getCreateTime());
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
             flag = customerDao.saveCustomer(customer);
            if (!flag){
              throw  new ClueConvertException("添加客户信息失败");
            }
        }

        //通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
         contacts.setOwner(clue.getOwner());
         contacts.setSource(clue.getSource());
         contacts.setCustomerId(customer.getId());
         contacts.setFullname(clue.getFullname());
         contacts.setAppellation(clue.getAppellation());
         contacts.setEmail(clue.getEmail());
         contacts.setMphone(clue.getMphone());
         contacts.setJob(clue.getJob());
         contacts.setCreateBy(clue.getCreateBy());
         contacts.setCreateTime(DateTimeUtil.getSysTime());
         contacts.setDescription(clue.getDescription());
         contacts.setContactSummary(clue.getContactSummary());
         contacts.setNextContactTime(clue.getNextContactTime());
         contacts.setAddress(clue.getAddress());
         flag = contactsDao.save(contacts);
         if (!flag ){
             throw  new ClueConvertException("添加联系人信息失败");
         }
        //线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clue.getId());
        for (ClueRemark clueRemark : clueRemarkList) {
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemark.setCreateBy(clue.getCreateBy());
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(customer.getId());
            flag = customerRemarkDao.save(customerRemark);
            if (!flag){
                throw  new ClueConvertException("添加客户备注信息失败");
            }
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            contactsRemark.setCreateBy(clue.getCreateBy());
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(contacts.getId());
             flag = contactsRemarkDao.save(contactsRemark);
            if (!flag){
                throw  new ClueConvertException("添加客户备注信息失败");
            }
        }
        //“线索和市场活动”的关系转换到“联系人和市场活动”的关系
            //找出与clueId关联的activityId
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getRelationByClueId(clue.getId());
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(activityId);
             flag = contactsActivityRelationDao.save(contactsActivityRelation);
            if (!flag){
                throw  new ClueConvertException("添加联系人和市场活动信息失败");
            }
        }
        //如果有创建交易需求，创建一条交易
        if (tran.getActivityId()!=null&&tran.getActivityId()!=""){
            //创建交易信息
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(contacts.getContactSummary());
            tran.setContactsId(contacts.getId());
             flag = tranDao.save(tran);
            if (!flag){
                throw  new ClueConvertException("添加创建交易信息失败");
            }
            //如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setCreateBy(clue.getCreateBy());
            tranHistory.setTranId(tran.getId());
            flag = tranHistoryDao.save(tranHistory);
            if (!flag){
                throw  new ClueConvertException("添加创建交易信息失败");
            }
        }

        //删除线索备注
        for (ClueRemark clueRemark : clueRemarkList) {
             flag = clueRemarkDao.deleteRemark(clueRemark.getId());
            if (!flag){
                throw  new ClueConvertException("添加创建交易信息失败");
            }
        }
        //删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
             flag = clueActivityRelationDao.deleteRelation(clueActivityRelation);
            if (!flag){
                throw  new ClueConvertException("添加创建交易信息失败");
            }
        }
        //删除线索
        flag = clueDao.delete(clue.getId());
        if (!flag){
            throw  new ClueConvertException("添加创建交易信息失败");
        }

    }
}
