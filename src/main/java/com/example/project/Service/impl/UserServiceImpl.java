package com.example.project.Service.impl;

import com.example.project.Service.UserService;
import com.example.project.dao.OrderDao;
import com.example.project.dao.UserDao;
import com.example.project.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/714:43
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserDao userDao;
    @Override
    public User getById(int id) {
        return userDao.getById(id);
    }

    @Override
    @Transactional
    public boolean tx() {
        User u1= new User();
        u1.setId(2);
        u1.setName("2222");
        userDao.insert(u1);

        User u2= new User();
        u2.setId(1);
        u2.setName("11111");
        userDao.insert(u2);
        return true;
    }
}
