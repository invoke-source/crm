package com.szx.crm.workbench.dao;

import com.szx.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/6/19 @16:13
 */
public interface ActivityDao {
    Integer saveActivity(Activity activity);

    Integer selectBytotal(Map<String, Object> map);

    List<Activity> selectByActivitys(Map<String, Object> map);

    int deleteByids(String[] ids);

    Activity selectById(String id);

    int updateActivity(Activity activity);

    Activity detail(String id);


    List<Activity> getActivityRelationListNotByClueId( Map<String,Object> map);

    List<Activity> getActivityList();

    List<Activity> getActivityListByName(String name);
}
