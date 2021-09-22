package com.szx.crm.settings.web.controller;

import com.szx.crm.settings.domain.User;
import com.szx.crm.settings.exception.LoginException;
import com.szx.crm.settings.service.UserService;
import com.szx.crm.utils.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @2021/6/17 @0:03
 */
@Controller
public class UserController {
    @Resource
    private UserService userService;
    @RequestMapping(value = "/settings/user/login.do")
    @ResponseBody
    public Map<String,Object> login(User user, HttpServletRequest request, HttpServletResponse response) throws LoginException {
        //获取浏览器的ip地址
        user.setAllowIps(request.getRemoteAddr());
        user.setLoginPwd(MD5Util.getMD5(user.getLoginPwd()));
        User u = userService.login(user);
        request.getSession().setAttribute("user",u);
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        return map;
    }

    @RequestMapping("/workbench/clue/getuserList.do")
    @ResponseBody
    public List<User> getUsers(){
        List<User> list = userService.getUserList();
        return list;
    }


}
