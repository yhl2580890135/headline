package cn.bulletjet.headline.controller;


import cn.bulletjet.headline.model.*;
import cn.bulletjet.headline.service.LikeService;
import cn.bulletjet.headline.service.NewsService;
import cn.bulletjet.headline.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    private List<ViewObject> getNews(int userId) {
        List<News> newsList = null;
        int localUserId = 0;
        if (hostHolder.getUser() != null)
            localUserId = hostHolder.getUser().getId();

        List<ViewObject> viewObjectList = new ArrayList<ViewObject>();
        if (userId == 0)
            newsList = newsService.getLatestNews();
        else
            newsList = userService.getUser(userId).getNews();

        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUser().getId()));
            if (localUserId != 0)
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            else
                vo.set("like", 0);
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
