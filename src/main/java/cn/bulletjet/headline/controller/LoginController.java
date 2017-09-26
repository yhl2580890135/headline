package cn.bulletjet.headline.controller;

import cn.bulletjet.headline.service.UserService;
import cn.bulletjet.headline.util.HeadlineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String register(Model model, @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "rember", defaultValue = "0") int remeber,
                           HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (remeber > 0)
                    cookie.setMaxAge(3600 * 24 * 7);
                response.addCookie(cookie);
                return HeadlineUtil.getJSONString(0, "注册成功");
            } else
                return HeadlineUtil.getJSONString(1, map);

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return HeadlineUtil.getJSONString(1, "注册异常");
        }


    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember", defaultValue = "0") int remeber,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (remeber > 0)
                    cookie.setMaxAge(3600 * 24 * 7);
                response.addCookie(cookie);
                return HeadlineUtil.getJSONString(0, "登录成功");

            } else
                return HeadlineUtil.getJSONString(1, map);

        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return HeadlineUtil.getJSONString(1, "登录异常");
        }
    }

    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}

