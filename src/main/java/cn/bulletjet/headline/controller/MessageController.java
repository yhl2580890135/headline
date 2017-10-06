package cn.bulletjet.headline.controller;

import cn.bulletjet.headline.model.HostHolder;
import cn.bulletjet.headline.model.Message;
import cn.bulletjet.headline.model.User;
import cn.bulletjet.headline.model.ViewObject;
import cn.bulletjet.headline.service.MessageService;
import cn.bulletjet.headline.service.UserService;
import cn.bulletjet.headline.util.HeadlineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<ViewObject> messages = new ArrayList<>();
            List<Message> conversationList = messageService.getDetails(conversationId);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                msg.setHasRead(1);
                messageService.addMessage(msg);
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getUsername());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = localUserId;
                if (localUserId == msg.getToId())
                    targetId = msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getUsername());
                vo.set("targetId", targetId);
                vo.set("totalCount", messageService.getTotalCount(localUserId, msg.getConversationId()));
                long unreadCount = messageService.getUnreadCount(localUserId, msg.getConversationId());
                vo.set("unreadCount", unreadCount);
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message msg = new Message();
            msg.setContent(content);
            msg.setCreatedDate(new Date());
            msg.setToId(toId);
            msg.setFromId(fromId);
            if (fromId < toId)
                msg.setConversationId(String.format("%d_%d", fromId, toId));
            else
                msg.setConversationId(String.format("%d_%d", toId, fromId));
            messageService.addMessage(msg);
            return HeadlineUtil.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
            return HeadlineUtil.getJSONString(1, "插入评论失败");
        }
    }

    @RequestMapping(path = {"/msg/deleteConversation"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String delete(@RequestParam("conversationId") String conversationId) {
        try {
            messageService.deleteConversation(conversationId);
            return "redirect:/msg/list";
            // return HeadlineUtil.getJSONString(0,"删除成功");
        } catch (Exception e) {
            logger.error("删除失败" + e.getMessage());
            //return HeadlineUtil.getJSONString(1,"删除失败");
            return "redirect:/msg/list";
        }

    }

    @RequestMapping(path = {"/msg/deleteMessage"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String delete(@RequestParam("id") int id) {
        String conId = messageService.findMessageById(id).getConversationId();
        try {

            messageService.deleteMessage(id);
            return "redirect:/msg/detail?conversationId=" + conId;
            // return HeadlineUtil.getJSONString(0,"删除成功");
        } catch (Exception e) {
            logger.error("删除失败" + e.getMessage());
            //return HeadlineUtil.getJSONString(1,"删除失败");
            return "redirect:/msg/detail?conversationId=" + conId;
        }

    }
}
