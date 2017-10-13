package cn.bulletjet.headline.controller;

import cn.bulletjet.headline.async.EventHandler;
import cn.bulletjet.headline.async.EventModel;
import cn.bulletjet.headline.async.EventProducer;
import cn.bulletjet.headline.async.EventType;
import cn.bulletjet.headline.model.EntityType;
import cn.bulletjet.headline.model.HostHolder;
import cn.bulletjet.headline.model.News;
import cn.bulletjet.headline.service.LikeService;
import cn.bulletjet.headline.service.NewsService;
import cn.bulletjet.headline.util.HeadlineUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class likeController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    NewsService newsService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {

        if (hostHolder.getUser() == null)
            return HeadlineUtil.getJSONString(1, "请先登录");
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
        newsService.updateLiketCount(newsId, (int) likeCount);
        News news = newsService.getById(newsId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(news.getUser().getId())
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId));
        return HeadlineUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("newsId") int newsId) {
        if (hostHolder.getUser() == null)
            return HeadlineUtil.getJSONString(1, "请先登录");
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId, EntityType.ENTITY_NEWS, newsId);
        newsService.updateLiketCount(newsId, (int) likeCount);
        return HeadlineUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
