package cn.bulletjet.headline.service;

import cn.bulletjet.headline.dao.LoginTicketDao;
import cn.bulletjet.headline.dao.UserDao;
import cn.bulletjet.headline.model.LoginTicket;
import cn.bulletjet.headline.model.User;
import cn.bulletjet.headline.util.HeadlineUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginTicketDao loginTicketDao;

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (username.isEmpty()) {
            map.put("msg_name", "用户名不能为空！");
            return map;
        }
        if (password.isEmpty()) {
            map.put("msg_password", "密码不能为空！");
            return map;
        }
        User user = userDao.findUserByUsername(username);
        if (user != null) {
            map.put("msg_name", "用户名已被注册");
            return map;
        }
        user = new User();
        user.setUsername(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(HeadlineUtil.MD5(password + user.getSalt()));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        userDao.save(user);

        return map;
    }

    private String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 3600 * 1000 * 72);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));// to produce a unique ticket
        loginTicketDao.save(loginTicket);
        return loginTicket.getTicket();
    }

    //此处应有登录逻辑
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (username.isEmpty()) {
            map.put("msg_name", "用户名不能为空！");
            return map;
        }
        if (password.isEmpty()) {
            map.put("msg_password", "密码不能为空！");
            return map;
        }
        User user = userDao.findUserByUsername(username);
        if (user == null) {
            map.put("msg_name", "用户名不存在");
            return map;
        }
        if (!HeadlineUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg_password", "密码错误，请重新输入");
            return map;
        }
        String loginTicket = addLoginTicket(user.getId());
        map.put("ticket", loginTicket);
        return map;
    }

    public void logout(String ticket) {
        loginTicketDao.updateStatusByTicket(1, ticket);
    }
    public User getUser(int id) {
        return userDao.findById(id).orElse(null);
    }

}
