package com.szx.crm.workbench.dao;

import com.szx.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsDao {

    Boolean save(Contacts contacts);

    Integer selectBytotal(Map<String, Object> map);

    List<Contacts> selectByContacts(Map<String, Object> map);

    Contacts getContactsById(String id);

    List<Contacts> getContactsListByName(String name);

    List<Contacts> getContactsList();
}
