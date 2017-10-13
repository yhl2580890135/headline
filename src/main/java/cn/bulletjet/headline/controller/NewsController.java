package cn.bulletjet.headline.controller;

import cn.bulletjet.headline.model.*;
import cn.bulletjet.headline.service.*;
import cn.bulletjet.headline.util.HeadlineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    @Autowired
    NewsService newsService;
    @Autowired
    CommentService commentService;
    @Autowired
    QiniuService qiniuService;

    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model) {
        try {
            News news = newsService.findById(newsId);
            if (news != null) {
                int localUserId = 0;
                if (hostHolder.getUser() != null)
                    localUserId = hostHolder.getUser().getId();
                if (localUserId != 0)
                    model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
                else
                    model.addAttribute("like", 0);
                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS, 0);
                List<ViewObject> commentVOs = new ArrayList<ViewObject>();
                for (Comment comment : comments) {
                    ViewObject commentVO = new ViewObject();
                    commentVO.set("comment", comment);
                    commentVO.set("user", userService.getUser(comment.getUser().getId()));
                    commentVOs.add(commentVO);
                }
                model.addAttribute("comments", commentVOs);
            }
            model.addAttribute("news", news);
            model.addAttribute("owner", news.getUser());

        } catch (Exception e) {
            logger.error("获取资讯明细错误" + e.getMessage());
        }
        return "detail";
    }

    @RequestMapping(path = {"/uploadImage/"}, method = RequestMethod.POST)
    @ResponseBody
    //上传图片 成功则返回图片的url
    public String uploadImage(@RequestParam("file") MultipartFile file) {//file包含图片的所有信息
        try {
            String fileURL = qiniuService.saveImage(file);
            if (fileURL == null)
                return HeadlineUtil.getJSONString(1, "上传失败");
            return HeadlineUtil.getJSONString(0, fileURL);
        } catch (Exception e) {
            logger.error("图片上传失败！" + e.getMessage());
            return HeadlineUtil.getJSONString(1, "上传失败");
        }
    }
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            if (hostHolder.getUser() != null) {
                news.setUser(hostHolder.getUser());
            } else {
                // 设置一个匿名用户,3是随便写的。
                news.setId(3);
            }
            newsService.addNews(news);
            return HeadlineUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯失败" + e.getMessage());
            return HeadlineUtil.getJSONString(1, "发布失败");
        }
    }


    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {
        try {

            content = HtmlUtils.htmlEscape(content);
            Comment comment = new Comment();
            comment.setUser(hostHolder.getUser());
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setEntityId(newsId);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            // 更新评论数量，以后用异步实现
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);

        } catch (Exception e) {
            logger.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

}
