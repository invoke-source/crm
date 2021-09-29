package com.szx.crm.workbench.dao;

import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    Boolean save(Tran tran);

    Integer selectBcytotal(Map<String, Object> map);

    List<Activity> selectByActivitys(Map<String, Object> map);

    Tran getTranById(String id);

    Boolean changeStage(Tran tran);

    List<String> getStageList();

    List<Map<String, Object>> dataLists();

    Integer getStageTotal();
}
