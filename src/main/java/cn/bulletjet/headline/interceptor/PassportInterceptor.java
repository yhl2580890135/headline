package cn.bulletjet.headline.interceptor;

import cn.bulletjet.headline.dao.LoginTicketDao;
import cn.bulletjet.headline.dao.UserDao;
import cn.bulletjet.headline.model.HostHolder;
import cn.bulletjet.headline.model.LoginTicket;
import cn.bulletjet.headline.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {
    @Autowired
    LoginTicketDao loginTicketDao;
    @Autowired
    UserDao userDao;
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDao.findByTicket(ticket);
            //status==0有效
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0)
                return true;
            User user = userDao.findById(loginTicket.getUserId()).orElse(null);
            hostHolder.setUser(user);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null)
            modelAndView.addObject("user", hostHolder.getUser());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        hostHolder.clear();
    }
}
