package cn.bulletjet.headline.dao;

import cn.bulletjet.headline.model.Comment;
import cn.bulletjet.headline.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentDao extends JpaRepository<Comment, Integer> {
    public List<Comment> findCommentByEntityIdAndEntityTypeAndStatusOrderByIdDesc(int entityId, int entityType, int status);

    @Transactional
    @Query("select count (c.id)from  Comment c where c.entityId=?1 and c.entityType=?2")
    public int getCommentCount(int entityId, int entityType);

    @Transactional
    @Modifying
    @Query("update Comment c set c.status=?2 where c.id=?1")
    public void updateStatus(int id, int satus);

}
