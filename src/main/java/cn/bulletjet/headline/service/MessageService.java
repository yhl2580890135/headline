package cn.bulletjet.headline.service;

import cn.bulletjet.headline.dao.MessageDao;
import cn.bulletjet.headline.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;

    public void addMessage(Message message) {
        messageDao.save(message);
    }

    public long getUnreadCount(int userId, String conId) {
        return messageDao.getConversationUnreadCount(userId, conId);
    }

    public long getTotalCount(int userId, String conId) {
        return messageDao.getTotalConversationCount(userId, conId);
    }

    public List<Message> getDetails(String conId) {
        return messageDao.findMessageByConversationIdOrderByIdDesc(conId);
    }

    public List<Message> getConversationList(int userId) {
        return messageDao.getConversationList(userId);
    }

    public void deleteConversation(String id) {
        messageDao.deleteMessageByConversationId(id);
    }

    public void deleteMessage(int id) {
        messageDao.deleteById(id);
    }

    public Message findMessageById(int id) {
        return messageDao.findMessagesById(id);
    }
}
