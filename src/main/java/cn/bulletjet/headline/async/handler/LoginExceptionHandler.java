package cn.bulletjet.headline.async.handler;


import cn.bulletjet.headline.async.EventHandler;
import cn.bulletjet.headline.async.EventModel;
import cn.bulletjet.headline.async.EventType;
import cn.bulletjet.headline.model.Message;
import cn.bulletjet.headline.service.MessageService;
import cn.bulletjet.headline.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;


    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次登陆异常");
        // SYSTEM ACCOUNT
        message.setFromId(3);
        if (3 < model.getEntityOwnerId())
            message.setConversationId(String.format("%d_%d", 3, model.getEntityOwnerId()));
        else
            message.setConversationId(String.format("%d_%d", model.getEntityOwnerId(), 3));
        message.setCreatedDate(new Date());
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("to"), "登陆异常", "mails/welcome.ftl", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
