package cn.bulletjet.headline;

import cn.bulletjet.headline.dao.UserDao;
import cn.bulletjet.headline.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDataBaseTests {
    @Autowired
    UserDao userDao;

    @Test
    public void contextLoads() {
//        for (int i = 0; i <10 ; i++) {
//           User user=new User();
//           user.setUsername("yhl"+i);
//           user.setPassword("month"+i);
//           user.setSalt("");
//           user.setHeadUrl("");
//           userDao.save(user);

        // }
        System.out.println(userDao.findById(0).orElse(null));

    }
}
