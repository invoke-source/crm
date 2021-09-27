package com.szx.crm.workbench.dao;

import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    Boolean  saveCustomer(Customer customer);

    Integer selectBytotal(Map<String, Object> map);

    List<Activity> selectByCustomers(Map<String, Object> map);

    Boolean save(Customer customer);

    Customer selectById(String id);

    Boolean updateCustomer(Customer customer);

    Integer deleteCustomerByid(String id);

    Customer getCustomerById(String id);

    List<String> getCustomerName(String name);
}
