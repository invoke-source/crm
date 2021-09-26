package com.szx.crm.workbench.service;

import com.szx.crm.settings.domain.User;
import com.szx.crm.workbench.domain.*;
import com.szx.crm.workbench.exception.ActivityClueRelationException;
import com.szx.crm.workbench.exception.ClueConvertException;
import com.szx.crm.workbench.exception.deleteCustomerException;
import com.szx.crm.workbench.vo.Pagination;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/16 @14:34
 */
public interface CustomerService {
     Pagination<Clue> PageList(Map<String, Object> map);

     List<User> getUserList();

    Boolean save(Customer customer);

    Customer selectById(String id);

    boolean updateCustomer(Customer customer);

    Boolean deleteCustomerByid(String[] id) throws deleteCustomerException;

    Customer detail(String id);

/*    Boolean save(Clue clue);

    Clue getClueById(String id);

    List<ClueRemark> getRemarkListByid(String id);

    Boolean saveRemark(ClueRemark clueRemark);

    ClueRemark getRemarkByid(String id);

    Boolean updateRemark(ClueRemark clueRemark);

    Boolean deleteRemark(String id);



    Boolean deleteRelation(ClueActivityRelation clueActivityRelation);

    List<Activity> getActivityRelationListNotByClueId( Map<String,Object> map);

    Boolean Relation(List<ClueActivityRelation> list) throws ActivityClueRelationException;*/

}
