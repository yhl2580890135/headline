package cn.bulletjet.headline;

import cn.bulletjet.headline.dao.NewsDao;
import cn.bulletjet.headline.dao.UserDao;
import cn.bulletjet.headline.model.News;
import cn.bulletjet.headline.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDataBaseTests {
    @Autowired
    UserDao userDao;
    @Autowired
    NewsDao newsDao;
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

            //user.setPassword("newpassword");
            // userDao.updatePassword(user);
        }

        Assert.assertEquals(null, userDao.findById(1).orElse(null).getPassword());
        userDao.deleteById(1);
        Assert.assertNull(userDao.findById(1));
    }


}

