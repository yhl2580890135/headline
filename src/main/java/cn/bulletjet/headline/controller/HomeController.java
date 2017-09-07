package cn.bulletjet.headline.controller;

import cn.bulletjet.headline.dao.NewsDao;
import cn.bulletjet.headline.model.News;
import cn.bulletjet.headline.model.ViewObject;
import cn.bulletjet.headline.service.NewsService;
import cn.bulletjet.headline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;

    @RequestMapping(path = {"/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String toIndex(Model model) {
        List<News> newsList = newsService.getLatestNews();
        List<ViewObject> viewObjectList = new ArrayList<ViewObject>();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getId()));
            viewObjectList.add(vo);
        }
        model.addAttribute("vos", viewObjectList);
        Date cur_date = new Date();
        cur_date.setTime(cur_date.getTime());
        model.addAttribute("cur_date", cur_date);
        return "home";
    }
}
