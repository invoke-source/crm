package com.szx.crm.workbench.dao;

import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    Boolean save(Clue clue);

    Integer selectBytotal(Map<String, Object> map);

    List<Activity> selectByActivitys(Map<String, Object> map);

    Clue getClueById(String id);
}
