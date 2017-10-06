package cn.bulletjet.headline.async.handler;


import cn.bulletjet.headline.async.EventHandler;
import cn.bulletjet.headline.async.EventModel;
import cn.bulletjet.headline.async.EventType;
import cn.bulletjet.headline.model.Message;
import cn.bulletjet.headline.model.User;
import cn.bulletjet.headline.service.MessageService;
import cn.bulletjet.headline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        User user = userService.getUser(model.getActorId());
        message.setToId(model.getEntityOwnerId());
        message.setContent("用户" + user.getUsername() +
                " 赞了你的资讯,http://127.0.0.1:8080/news/"
                + String.valueOf(model.getEntityId()));
        // SYSTEM ACCOUNT
        message.setFromId(3);
        if (3 < model.getEntityOwnerId())
            message.setConversationId(String.format("%d_%d", 3, model.getEntityOwnerId()));
        else
            message.setConversationId(String.format("%d_%d", model.getEntityOwnerId(), 3));
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
