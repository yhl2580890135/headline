package cn.bulletjet.headline;

import cn.bulletjet.headline.dao.LoginTicketDao;
import cn.bulletjet.headline.dao.NewsDao;
import cn.bulletjet.headline.dao.UserDao;
import cn.bulletjet.headline.model.LoginTicket;
import cn.bulletjet.headline.model.News;
import cn.bulletjet.headline.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Rollback(value = true)
public class InitDataBaseTests {
    @Autowired
    UserDao userDao;
    @Autowired
    NewsDao newsDao;
    @Autowired
    LoginTicketDao loginTicketDao;
    @Test
    public void contextLoads() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {

            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setUsername(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDao.save(user);

            News news = new News();
            news.setUser(user);
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i + 1);
            news.setId(i + 1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDao.save(news);
            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setStatus(0);
            loginTicket.setUserId(i + 1);
            loginTicket.setExpired(date);
            loginTicket.setTicket(String.format("TICKET{%d}", i));
            loginTicketDao.save(loginTicket);
            loginTicketDao.updateTicketByStatus(2, loginTicket.getTicket());


        }

    }


}

