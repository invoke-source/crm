package com.szx.crm.settings.service;

import com.szx.crm.settings.domain.User;
import com.szx.crm.settings.exception.LoginException;

import java.util.List;

/**
 * @author Administrator
 * @2021/6/16 @23:58
 */
public interface UserService {

    User login(User user) throws LoginException;
    List<User> getUserList();
}
