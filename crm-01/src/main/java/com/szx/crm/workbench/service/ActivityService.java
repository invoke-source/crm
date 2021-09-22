package com.szx.crm.workbench.service;

import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.ActivityRemark;
import com.szx.crm.workbench.exception.ActivityDeleteException;
import com.szx.crm.workbench.exception.ActivityException;
import com.szx.crm.workbench.exception.ActivityRemarkDeleteException;
import com.szx.crm.workbench.exception.ActivityRemarkSaveException;
import com.szx.crm.workbench.vo.Pagination;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/6/19 @16:23
 */
public interface ActivityService {
    boolean saveActivity(Activity activity) throws ActivityException;

    Pagination<Activity> PageList(Map<String,Object> map);

    boolean deleteActivity(String[] ids) throws  ActivityDeleteException;

    Activity selectById(String id);

    boolean updateActivity(Activity activity) throws ActivityException;

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByid(String id);

    Boolean deleteRemark(String id) throws ActivityRemarkDeleteException;

    Boolean saveRemark(ActivityRemark activityRemark) throws ActivityRemarkSaveException;

    ActivityRemark getRemarkByid(String id);

    Boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityListByClueId(String clueId);
}
