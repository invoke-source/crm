package com.szx.crm.workbench.web.controller;

import com.szx.crm.settings.domain.User;
import com.szx.crm.settings.service.UserService;
import com.szx.crm.utils.DateTimeUtil;
import com.szx.crm.utils.UUIDUtil;
import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.ActivityRemark;
import com.szx.crm.workbench.exception.ActivityDeleteException;
import com.szx.crm.workbench.exception.ActivityException;
import com.szx.crm.workbench.exception.ActivityRemarkDeleteException;
import com.szx.crm.workbench.exception.ActivityRemarkSaveException;
import com.szx.crm.workbench.service.ActivityService;
import com.szx.crm.workbench.vo.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/6/19 @16:25
 */
@Controller
public class ActivityController {
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;


    @RequestMapping("/workbench/activity/getuserList.do")
    @ResponseBody
    public List<User> getUsers(){
        List<User> list = userService.getUserList();
        return list;
    }


    @RequestMapping(value = "/workbench/activity/save.do")
    @ResponseBody
    public Map<String, Boolean> save(Activity activity, HttpServletRequest request) throws ActivityException {
      activity.setId(UUIDUtil.getUUID());
      activity.setCreateTime(DateTimeUtil.getSysTime());
      User user = (User) request.getSession().getAttribute("user");
      activity.setCreateBy(user.getName());
      boolean success = activityService.saveActivity(activity);
      Map<String,Boolean> map = new HashMap<>(16);
      map.put("success",true);
     return map;
    }

    @RequestMapping(value = "/workbench/activity/pageList.do" )
    @ResponseBody
    public Pagination<Activity> pageList(Activity activity, String pageNo, String pageSize)  {
        Map<String,Object> map = new HashMap<>();
        Integer kipCount = (Integer.valueOf(pageNo)-1)*Integer.valueOf(pageSize);
        map.put("skipCount",kipCount);
        map.put("pageSize",Integer.valueOf(pageSize));
        map.put("name",activity.getName());
        map.put("owner",activity.getOwner());
        map.put("startDate",activity.getStartDate());
        map.put("endDate",activity.getEndDate());
        /*获取vo对象*/
        Pagination pagination = activityService.PageList(map);

        return pagination;
    }

    //删除记录
    @RequestMapping(value = "/workbench/activity/delete.do")
    @ResponseBody
    public Map<String,Boolean> delete(HttpServletRequest request, HttpServletResponse response) throws ActivityDeleteException {
        //接受要删除记录的id值
        String[] ids = request.getParameterValues("id");
        //是否删除成功
        Boolean flag = activityService.deleteActivity(ids);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",flag);
        return map;
    }

    @RequestMapping(value = "/workbench/activity/getuserListActivity.do")
    @ResponseBody
    public Map<String,Object> selectByid(String id){
        List<User> list = userService.getUserList();
        Activity activity = activityService.selectById(id);
        Map<String,Object> map = new HashMap<>(16);
        map.put("activity",activity);
        map.put("list",list);
        return map;
    }

    //修改记录
    @RequestMapping(value = "/workbench/activity/update.do")
    @ResponseBody
    public Map<String, Boolean> update(Activity activity, HttpServletRequest request) throws ActivityException {
        activity.setCreateTime(DateTimeUtil.getSysTime());
        activity.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        boolean success = activityService.updateActivity(activity);
        Map<String,Boolean> map = new HashMap<>(16);
        map.put("success",success);
        return map;
    }

    //跳转详细信息页
    @RequestMapping(value = "workbench/activity/detail.do")
    public ModelAndView detail(String id){
        Activity activity = activityService.detail(id);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/workbench/activity/detail.jsp");
        mv.addObject("activity",activity);
        return mv;
    }

    @RequestMapping(value = "workbench/activity/getRemarkListByid.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkListByid(String id){
        //获取备注列表
        List<ActivityRemark> list = activityService.getRemarkListByid(id);
        return list;
    }

    //删除备注
    @RequestMapping(value = "workbench/activity/deleteRemark.do")
    @ResponseBody
    public Map<String,Object> deleteRemark(String id) throws ActivityRemarkDeleteException {
        Boolean flag = activityService.deleteRemark(id);
        Map<String,Object> map = new HashMap<>(2);
        map.put("success",flag);
        return map;
    }

    //保存备注信息
    @RequestMapping(value = "workbench/activity/saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(ActivityRemark activityRemark, HttpServletRequest request) throws ActivityRemarkSaveException {
        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        User user = (User) request.getSession().getAttribute("user");
        activityRemark.setCreateBy(user.getName());
        activityRemark.setEditFlag("0");
        Boolean success = activityService.saveRemark(activityRemark);
        Map<String,Object> map = new HashMap<>(2);
        map.put("success",success);
        map.put("activityRemark",activityRemark);
        return map;
    }

    //获取指定id的备注信息
    @RequestMapping(value = "workbench/activity/getRemarkByid.do")
    @ResponseBody
    public ActivityRemark getRemarkByid(String id){
        ActivityRemark activityRemark = activityService.getRemarkByid(id);
        return activityRemark;
    }

    //修改备注
    @RequestMapping(value = "workbench/activity/updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(ActivityRemark activityRemark, HttpServletRequest request){
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        User user = (User) request.getSession().getAttribute("user");
        activityRemark.setEditBy(user.getName());
        activityRemark.setEditFlag("1");
        Boolean flag = activityService.updateRemark(activityRemark);
        Map<String,Object> map = new HashMap<>(2);
        map.put("success",flag);
        map.put("activityRemark",activityRemark);
        return map;
    }
}
