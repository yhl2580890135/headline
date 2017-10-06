package cn.bulletjet.headline.service;

import cn.bulletjet.headline.dao.CommentDao;
import cn.bulletjet.headline.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    @Autowired
    CommentDao commentDao;

    public List<Comment> getCommentsByEntity(int entityId, int entityType, int status) {
        return commentDao.findCommentByEntityIdAndEntityTypeAndStatusOrderByIdDesc(entityId, entityType, status);
    }

    public void addComment(Comment comment) {
        commentDao.save(comment);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }

    public void deleteComment(int id, int status) {
        commentDao.updateStatus(id, status);
    }
}
