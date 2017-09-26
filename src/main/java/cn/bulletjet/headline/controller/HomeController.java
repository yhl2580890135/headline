package cn.bulletjet.headline.controller;


import cn.bulletjet.headline.model.News;
import cn.bulletjet.headline.model.ViewObject;
import cn.bulletjet.headline.service.NewsService;
import cn.bulletjet.headline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;


    private List<ViewObject> getNews(int userId) {
        List<News> newsList = null;
        List<ViewObject> viewObjectList = new ArrayList<ViewObject>();
        if (userId == 0)
            newsList = newsService.getLatestNews();
        else
            newsList = userService.getUser(userId).getNews();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getId()));
            viewObjectList.add(vo);
        }

        return viewObjectList;

    }

    @RequestMapping(path = {"/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop) {
        Date cur_date = new Date();
        cur_date.setTime(cur_date.getTime());
        model.addAttribute("cur_date", cur_date);
        model.addAttribute("vos", getNews(0));
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        Date cur_date = new Date();
        cur_date.setTime(cur_date.getTime());
        model.addAttribute("cur_date", cur_date);
        model.addAttribute("vos", getNews(userId));
        return "home";
    }

}
