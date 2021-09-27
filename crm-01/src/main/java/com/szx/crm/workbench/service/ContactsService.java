package com.szx.crm.workbench.service;

import com.szx.crm.settings.domain.User;
import com.szx.crm.workbench.domain.Clue;
import com.szx.crm.workbench.domain.Contacts;
import com.szx.crm.workbench.domain.Customer;
import com.szx.crm.workbench.exception.deleteCustomerException;
import com.szx.crm.workbench.vo.Pagination;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/16 @14:34
 */
public interface ContactsService {
 List<Contacts> getContactsListByName(String name);


    List<Contacts> getContactsList() ;


    Pagination<Contacts> PageList(Map<String, Object> map);

     List<User> getUserList();

    Boolean save(Contacts contacts);

    Customer selectById(String id);

    boolean updateCustomer(Customer customer);

    Boolean deleteCustomerByid(String[] id) throws deleteCustomerException;

    Contacts detail(String id);

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
