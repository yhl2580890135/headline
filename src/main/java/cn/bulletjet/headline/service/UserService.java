package cn.bulletjet.headline.service;

import cn.bulletjet.headline.dao.UserDao;
import cn.bulletjet.headline.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getUser(int id) {
        return userDao.findById(id).orElse(null);
    }

}
