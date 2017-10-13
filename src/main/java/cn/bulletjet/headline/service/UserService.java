package cn.bulletjet.headline.service;


import cn.bulletjet.headline.dao.LoginTicketDao;
import cn.bulletjet.headline.dao.UserDao;
import cn.bulletjet.headline.model.LoginTicket;
import cn.bulletjet.headline.model.User;
import cn.bulletjet.headline.util.HeadlineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginTicketDao loginTicketDao;
    @Autowired
    private QiniuService qiniuService;
    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (username.equals("")) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (password.equals("")) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDao.findUserByUsername(username);

        if (user != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }

        // 密码强度
        user = new User();
        user.setUsername(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(HeadlineUtil.MD5(password + user.getSalt()));
        userDao.save(user);
        // 登陆
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }


    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (username.equals("")) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (password.equals("")) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDao.findUserByUsername(username);

        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }

        if (!HeadlineUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("userId", user.getId());
            map.put("msgpwd", "密码不正确");
            return map;
        }
        map.put("userId", user.getId());
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDao.save(ticket);
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDao.findById(id).orElse(null);
    }

    public User getUserByUsername(String name) {
        return userDao.findUserByUsername(name);
    }

    public void logout(String ticket) {
        loginTicketDao.updateStatusByTicket(1, ticket);
    }
}