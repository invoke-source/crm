package com.szx.crm.workbench.web.controller;

import com.szx.crm.settings.domain.User;
import com.szx.crm.utils.DateTimeUtil;
import com.szx.crm.utils.UUIDUtil;
import com.szx.crm.workbench.domain.Clue;
import com.szx.crm.workbench.domain.Contacts;
import com.szx.crm.workbench.domain.Customer;
import com.szx.crm.workbench.exception.deleteCustomerException;
import com.szx.crm.workbench.service.ContactsService;
import com.szx.crm.workbench.service.CustomerService;
import com.szx.crm.workbench.vo.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/17 @17:17
 */
@Controller
public class ContactsController {
    @Resource
    private ContactsService contactsService;


    @RequestMapping(value = "/workbench/contacts/save.do")
    @ResponseBody
    public Map<String,Boolean> save(Contacts contacts, HttpServletRequest request){
        contacts.setId(UUIDUtil.getUUID());
        User user = (User) request.getSession().getAttribute("user");
        contacts.setCreateBy(user.getName());
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        Boolean flag = contactsService.save(contacts);
        Map<String,Boolean> map = new HashMap<>(1);
        map.put("success",flag);
        return map;
    }

    @RequestMapping("/workbench/contacts/getuserList.do")
    @ResponseBody
    public List<User> getUsers(){
        List<User> list = contactsService.getUserList();
        return list;
    }
//
//    @RequestMapping(value = "/workbench/customer/getuserListActivity.do")
//    @ResponseBody
//    public Map<String,Object> selectByid(String id){
//        List<User> list = customerService.getUserList();
//        Customer customer  = customerService.selectById(id);
//        Map<String,Object> map = new HashMap<>(16);
//        map.put("customer",customer);
//        map.put("list",list);
//        return map;
//    }
//
//    //修改记录
//    @RequestMapping(value = "/workbench/customer/update.do")
//    @ResponseBody
//    public Map<String, Boolean> update(Customer customer, HttpServletRequest request) {
//        customer.setEditBy(DateTimeUtil.getSysTime());
//        customer.setEditTime(((User)request.getSession().getAttribute("user")).getName());
//        boolean success = customerService.updateCustomer(customer);
//        Map<String,Boolean> map = new HashMap<>(16);
//        map.put("success",success);
//        return map;
//    }
//
    @RequestMapping(value = "/workbench/contacts/pageList.do" )
    @ResponseBody
    public Pagination<Clue> pageList(Contacts contacts,String customerName, String pageNo, String pageSize )  {
        Map<String,Object> map = new HashMap<>();
        Integer kipCount = (Integer.valueOf(pageNo)-1)*Integer.valueOf(pageSize);
        map.put("skipCount",kipCount);
        map.put("pageSize",Integer.valueOf(pageSize));
        map.put("owner",contacts.getOwner());
        map.put("fullname",contacts.getFullname());
        map.put("name",customerName);
        map.put("source",contacts.getSource());
        map.put("birth",contacts.getBirth());
        /*获取vo对象*/
        Pagination pagination = contactsService.PageList(map);

        return pagination;
    }
//
//    @RequestMapping(value = "/workbench/customer/deleteCustomerByid.do" )
//    @ResponseBody
//    public Boolean deleteCustomerByid(String[] id) throws deleteCustomerException {
//            Boolean success = customerService.deleteCustomerByid(id);
//            return success;
//    }

    @RequestMapping(value = "/workbench/contacts/detail.do")
    @ResponseBody
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Contacts contacts = contactsService.detail(id);
        mv.addObject("contacts",contacts);
        mv.setViewName("/workbench/contacts/detail.jsp");
        return mv;
    }



























    /*@RequestMapping(value = "workbench/clue/detail.do")
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
*/





}
