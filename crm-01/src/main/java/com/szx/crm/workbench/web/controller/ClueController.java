package com.szx.crm.workbench.web.controller;

import com.szx.crm.settings.domain.User;
import com.szx.crm.utils.DateTimeUtil;
import com.szx.crm.utils.UUIDUtil;
import com.szx.crm.workbench.domain.*;
import com.szx.crm.workbench.exception.ActivityClueRelationException;
import com.szx.crm.workbench.exception.ClueConvertException;
import com.szx.crm.workbench.service.ActivityService;
import com.szx.crm.workbench.service.ClueService;
import com.szx.crm.workbench.vo.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/17 @17:17
 */
@Controller
public class ClueController {
    @Resource
    private ClueService clueService;
    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "/workbench/clue/save.do")
    @ResponseBody
    public Map<String,Boolean> save(Clue clue, HttpServletRequest request){
        clue.setId(UUIDUtil.getUUID());
        User user = (User) request.getSession().getAttribute("user");
        clue.setCreateBy(user.getName());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        Boolean flag = clueService.save(clue);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",flag);
        return map;
    }

    @RequestMapping(value = "/workbench/clue/pageList.do" )
    @ResponseBody
    public Pagination<Clue> pageList(Clue clue, String pageNo, String pageSize )  {
        Map<String,Object> map = new HashMap<>();
        Integer kipCount = (Integer.valueOf(pageNo)-1)*Integer.valueOf(pageSize);
        map.put("skipCount",kipCount);
        map.put("pageSize",Integer.valueOf(pageSize));
        map.put("appellation",clue.getAppellation());
        map.put("company",clue.getCompany());
        map.put("phone",clue.getPhone());
        map.put("source",clue.getSource());
        map.put("owner",clue.getOwner());
        map.put("mphone",clue.getMphone());
        map.put("state",clue.getState());
        /*获取vo对象*/
        Pagination pagination = clueService.PageList(map);

        return pagination;
    }

    @RequestMapping(value = "workbench/clue/detail.do")
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Clue clue = clueService.getClueById(id);
        mv.setViewName("/workbench/clue/detail.jsp");
        mv.addObject("clue",clue);
        return mv;
    }

    @RequestMapping(value = "workbench/clue/getRemarkListByid.do")
    @ResponseBody
    public List<ClueRemark> getRemarkListByid(String id){
        List<ClueRemark> list = clueService.getRemarkListByid(id);
        return list;

    }
    @RequestMapping(value = "workbench/clue/saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(ClueRemark clueRemark, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>(2);
        clueRemark.setId(UUIDUtil.getUUID());
        User user = (User) request.getSession().getAttribute("user");
        clueRemark.setCreateBy(user.getName());
        clueRemark.setCreateTime(DateTimeUtil.getSysTime());
        clueRemark.setEditFlag("0");
        Boolean success = clueService.saveRemark(clueRemark);
        map.put("clueRemark",clueRemark);
        map.put("success",success);
        return map;
    }

    @RequestMapping(value = "workbench/clue/getRemarkByid.do")
    @ResponseBody
    public ClueRemark getRemarkByid(String id){
        ClueRemark clueRemark = clueService.getRemarkByid(id);
        return clueRemark;
    }


    @RequestMapping(value = "workbench/clue/updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(ClueRemark clueRemark, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>(2);
        clueRemark.setEditTime(DateTimeUtil.getSysTime());
        User user = (User) request.getSession().getAttribute("user");
        clueRemark.setEditBy(user.getName());
        clueRemark.setEditFlag("1");
        Boolean success= clueService.updateRemark(clueRemark);
        map.put("success",success);
        map.put("clueRemark",clueRemark);
        return map;
    }

    @RequestMapping(value = "workbench/clue/deleteRemark.do")
    @ResponseBody
    public Map<String,Object> deleteRemark(String id){
        Boolean flag = clueService.deleteRemark(id);
        Map<String,Object> map = new HashMap<>(1);
        map.put("success",flag);
        return map;
    }

    @RequestMapping(value = "workbench/clue/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId){
        List<Activity> list = activityService.getActivityListByClueId(clueId);
        return list;
    }

    @RequestMapping(value = "workbench/clue/deleteRelation.do")
    @ResponseBody
    public Boolean deleteRelation(ClueActivityRelation clueActivityRelation){
        Boolean success = clueService.deleteRelation(clueActivityRelation);
        return success;
    }

    @RequestMapping(value = "workbench/clue/getActivityRelationListNotByClueId.do")
    @ResponseBody
    public List<Activity> getActivityRelationListNotByClueId(String clueId ,String qname){
        Map<String,Object> map = new HashMap<>(2);
        map.put("clueId",clueId);
        map.put("qname",qname);
        List<Activity> list = clueService.getActivityRelationListNotByClueId(map);
        return list;
    }

    @RequestMapping(value = "workbench/clue/Relation.do")
    @ResponseBody
    public Map<String,Object> Relation(String[] activityId,String clueId) throws ActivityClueRelationException {
        List<ClueActivityRelation> list = new ArrayList<>(activityId.length);
        for (String s : activityId) {
            ClueActivityRelation c = new ClueActivityRelation();
            c.setId(UUIDUtil.getUUID());
            c.setClueId(clueId);
            c.setActivityId(s);
            list.add(c);
        }
        Boolean success = clueService.Relation(list);
        Map<String,Object> map = new HashMap<>(1);
        map.put("success",success);
        return map;
    }

    @RequestMapping(value = "workbench/clue/getActivityList.do")
    @ResponseBody
    public List<Activity> getActivityList(){
        List<Activity> list = activityService.getActivityList();
        return list;
    }


    @RequestMapping(value = "workbench/clue/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String name){
        List<Activity> list = activityService.getActivityListByName(name);
        return list;
    }

    //线索转换
    @RequestMapping(value = "workbench/clue/convert.do")
    public String convert(Tran tran,@RequestParam(value = "clueId") String id, HttpServletRequest request) throws ClueConvertException {
        tran.setId(UUIDUtil.getUUID());
        User user = (User) request.getSession().getAttribute("user");
        tran.setCreateBy(user.getName());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        clueService.convert(id,tran);

        return "/workbench/clue/index.jsp";

    }





}
