package com.szx.crm.workbench.web.controller;

import com.szx.crm.settings.domain.User;
import com.szx.crm.settings.service.UserService;
import com.szx.crm.utils.DateTimeUtil;
import com.szx.crm.utils.UUIDUtil;
import com.szx.crm.workbench.domain.Activity;
import com.szx.crm.workbench.domain.Contacts;
import com.szx.crm.workbench.domain.Tran;
import com.szx.crm.workbench.domain.TranHistory;
import com.szx.crm.workbench.exception.ChangeStageExceeption;
import com.szx.crm.workbench.exception.TranSaveException;
import com.szx.crm.workbench.service.ActivityService;
import com.szx.crm.workbench.service.ContactsService;
import com.szx.crm.workbench.service.CustomerService;
import com.szx.crm.workbench.service.TranService;
import com.szx.crm.workbench.vo.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/9/26 @14:02
 */
@Controller
public class TranController {
    @Resource
    private CustomerService customerService;
    @Resource
    private TranService tranService;
    @Resource
    private UserService userService;
    @Resource
    private ContactsService contactsService;
    @Resource
    private ActivityService activityService;

    @RequestMapping(value = "workbench/transaction/add.do")
    public ModelAndView add(){
        ModelAndView mv = new ModelAndView();
        List<User> ulist = userService.getUserList();
        mv.setViewName("/workbench/transaction/save.jsp");
        mv.addObject("ulist",ulist);
        return mv;
    }

    @RequestMapping(value = "workbench/transaction/getActivityList.do")
    @ResponseBody
    public List<Activity> getActivityList(){
        List<Activity> list = activityService.getActivityList();
        return list;
    }


    @RequestMapping(value = "workbench/transaction/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String name){
        List<Activity> list = activityService.getActivityListByName(name);
        return list;
    }

    @RequestMapping(value = "workbench/transaction/getContactsList.do")
    @ResponseBody
    public List<Contacts> getContactsList(){
        List<Contacts> list = contactsService.getContactsList();
        return list;
    }


    @RequestMapping(value = "workbench/transaction/getContactsListByName.do")
    @ResponseBody
    public List<Contacts> getContactsListByName(String name){
        List<Contacts> list = contactsService.getContactsListByName(name);
        return list;
    }
    //获取客户名称列表
    @RequestMapping(value = "workbench/transaction/getCustomerName.do")
    @ResponseBody
    public List<String>  getCustomerName(String name){
        List<String> customerNameList = customerService.getCustomerName(name);
        return customerNameList;
    }

    //展示交易信息列表
    @RequestMapping(value = "workbench/transaction/pageList.do")
    @ResponseBody
    public Pagination pageList(Tran tran,String customerName,String contactsName,String pageNo,String pageSize){
        Map<String,Object> map = new HashMap<>();
        Integer kipCount = (Integer.valueOf(pageNo)-1)*Integer.valueOf(pageSize);
        map.put("skipCount",kipCount);
        map.put("pageSize",Integer.valueOf(pageSize));
        map.put("ownerowner",tran.getOwner());
        map.put("namename",tran.getName());
        map.put("customerName",customerName);
        map.put("stagestage",tran.getStage());
        map.put("typetype",tran.getType());
        map.put("sourcesource",tran.getSource());
        map.put("contactsName",contactsName);
        /*获取vo对象*/
        Pagination pagination = tranService.PageList(map);

        return pagination;

    }

    //交易表的添加操作
    @RequestMapping(value = "workbench/transaction/saveTran.do")
    public String savaTran(Tran tran, String customerName, HttpServletRequest request) throws TranSaveException {

        tran.setId(UUIDUtil.getUUID());
        User user = (User) request.getSession().getAttribute("user");
        tran.setCreateBy(user.getName());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        tranService.savaTran(tran,customerName);
        return "/workbench/transaction/index.jsp";
    }

    @RequestMapping(value = "workbench/transaction/detail.do")
    @ResponseBody
    public ModelAndView detail(String id,HttpServletRequest request){
        Tran tran = tranService.getTranById(id);
        ModelAndView mv = new ModelAndView();
        ServletContext servletContext = request.getServletContext();
        Map<String,String> map = (Map<String, String>) servletContext.getAttribute("pmap");
        String possibility = map.get(tran.getStage());
        tran.setPossibility(possibility);
        mv.addObject("tran",tran);
        mv.setViewName("/workbench/transaction/detail.jsp");
        return mv;
    }

    @RequestMapping(value = "workbench/transaction/showHistoryList.do")
    @ResponseBody
    public List<TranHistory> showHistoryList(String tranId, HttpServletRequest request){
        ServletContext servletContext = request.getServletContext();
        Map<String,String> map = (Map<String, String>) servletContext.getAttribute("pmap");
        List<TranHistory> list = tranService.showHistoryList(tranId,map);
        return list;

    }

    //修改交易表的阶段信息
    @RequestMapping(value = "workbench/transaction/changeStage.do")
    @ResponseBody
    public Map<String,Object> changeStage(TranHistory tranHistory,String Tranid,HttpServletRequest request) throws ChangeStageExceeption {
        User user = (User) request.getSession().getAttribute("user");
        tranHistory.setCreateBy(user.getName());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setTranId(Tranid);
        tranHistory.setId(UUIDUtil.getUUID());
        //修改交易表的阶段信息
        ServletContext servletContext = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) servletContext.getAttribute("pmap");
        Boolean flag = tranService.changeStage(Tranid,tranHistory);
        Tran tran = tranService.getTranById(Tranid);
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);
        Map<String,Object> map = new HashMap<>(2);
        map.put("success",flag);
        map.put("tran",tran);
        return map;
    }
}
