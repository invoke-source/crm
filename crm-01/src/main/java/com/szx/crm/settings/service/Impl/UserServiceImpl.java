package com.szx.crm.settings.service.Impl;

import com.szx.crm.settings.dao.UserDao;
import com.szx.crm.settings.domain.User;
import com.szx.crm.settings.exception.LoginException;
import com.szx.crm.settings.service.UserService;
import com.szx.crm.utils.DateTimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * @2021/6/16 @23:58
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
   /**@Transactional( propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,readOnly = true)*/
    public User login(User user) throws LoginException {
        User use =userDao.login(user);
        if (use == null){
            throw new LoginException("账号密码错误");
        }
        if (use.getExpireTime().compareTo(DateTimeUtil.getSysTime()) < 0){
            throw new LoginException("账号已失效");
        }
        if ("0".equals(use.getLockState())){
            throw new LoginException("账号已被锁定");
        }
        if (!use.getAllowIps().contains(user.getAllowIps())){
            throw new LoginException("账号受到限制");
        }
        return use;

    }

    @Override
    public List<User> getUserList() {
        List<User> list = userDao.getUserList();
        return list;
    }




}
