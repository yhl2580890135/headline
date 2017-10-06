package cn.bulletjet.headline.dao;

import cn.bulletjet.headline.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessageDao extends JpaRepository<Message, Integer> {
    @Transactional
    @Query(value = "select count (m) from  Message m  where m.hasRead=0 and m.toId=?1 and m.conversationId=?2 ")
    public long getConversationUnreadCount(int userId, String convId);

    @Transactional
    @Query(value = "select count(id)  from  Message  where toId=?1 and conversationId=?2 ")
    public long getTotalConversationCount(int userId, String convId);

    public List<Message> findMessageByConversationIdOrderByIdDesc(String conId);

    @Transactional
    @Query(value = "SELECT message_temp.* FROM (( SELECT conversation_id   AS conversation_id,max(created_date) AS created_data FROM message where to_id=?1 OR from_id =?1 GROUP BY conversation_id ORDER BY created_date DESC) AS id_and_maxtime\n" +
            "  LEFT JOIN message AS message_temp ON id_and_maxtime.conversation_id=message_temp.conversation_id && id_and_maxtime.created_data=message_temp.created_date)ORDER BY message_temp.created_date DESC ;\n", nativeQuery = true)
    public List<Message> getConversationList(int userId);

    @Transactional
    public void deleteMessageByConversationId(String id);

    public Message findMessagesById(int id);
}