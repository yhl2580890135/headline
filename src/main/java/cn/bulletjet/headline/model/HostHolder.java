package cn.bulletjet.headline.model;

import org.springframework.stereotype.Component;

/**
 * Created by nowcoder on 2016/7/3.
 */
@Component
//定义当前的用户是谁
public class HostHolder {
    //线程本地变量 每个用户只能取得自身相关的东西
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();

    public User getUser() {
        return userThreadLocal.get();
    }

    public void setUser(User user) {
        userThreadLocal.set(user);
    }

    public void clear() {
        userThreadLocal.remove();
    }
}
