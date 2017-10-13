package cn.bulletjet.headline.controller;


import cn.bulletjet.headline.dao.NewsDao;
import cn.bulletjet.headline.model.*;
import cn.bulletjet.headline.service.LikeService;
import cn.bulletjet.headline.service.NewsService;
import cn.bulletjet.headline.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.data.querydsl.QSort.*;


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
    @Autowired
    NewsDao newsDao;

    private List<ViewObject> getNews(int userId) {
        List<News> newsList = null;
        int localUserId = 0;
        if (hostHolder.getUser() != null)
            localUserId = hostHolder.getUser().getId();
        List<ViewObject> viewObjectList = new ArrayList<ViewObject>();
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

//    @RequestMapping(path = {"/"}, method = {RequestMethod.GET, RequestMethod.POST})
//    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop) {
//        Date cur_date = new Date();
//        cur_date.setTime(cur_date.getTime());
//        model.addAttribute("cur_date", cur_date);
//        model.addAttribute("vos", getNews(0));
//        model.addAttribute("pop", pop);
//        return "home";
//    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        Date cur_date = new Date();
        cur_date.setTime(cur_date.getTime());
        model.addAttribute("cur_date", cur_date);
        model.addAttribute("vos", getNews(userId));
        model.addAttribute("last", getNews(userId).size() / 5);
        return "home";
    }

    @RequestMapping(path = {"/"}, method = {RequestMethod.GET})
    public String getEntryByPageable(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum, @PageableDefault(value = 5, sort = {"createdDate"}, direction = Sort.Direction.DESC)
            Pageable pageable, Model model, @RequestParam(value = "pop", defaultValue = "0") int pop) {

        Page<News> page1 = newsDao.findAll(pageable);
        model.addAttribute("last", page1.getTotalPages() - 1);
        for (int i = 0; i < pageNum; i++) {
            pageable = pageable.next();
        }
        page1 = newsDao.findAll(pageable);
        int localUId = 0;
        if (hostHolder.getUser() != null)
            localUId = hostHolder.getUser().getId();
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for (News newss : page1) {
            ViewObject v = new ViewObject();
            v.set("user", userService.getUser(newss.getUser().getId()));
            v.set("news", newss);
            if (localUId != 0)
                v.set("like", likeService.getLikeStatus(localUId, EntityType.ENTITY_NEWS, newss.getId()));
            else
                v.set("like", 0);
            vos.add(v);
        }
        Date cur_date = new Date();
        cur_date.setTime(cur_date.getTime() + 360000000);
        model.addAttribute("cur_date", cur_date);
        model.addAttribute("vos", vos);
        model.addAttribute("pop", pop);
        return "home";

    }
}