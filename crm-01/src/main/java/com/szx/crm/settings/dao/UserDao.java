package com.szx.crm.settings.dao;

import com.szx.crm.settings.domain.User;

import java.util.List;

/**
 * @author Administrator
 * @2021/6/16 @23:55
 */
public interface UserDao {
    User login(User user);
    List<User> getUserList();
}
