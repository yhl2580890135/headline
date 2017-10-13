package cn.bulletjet.headline.dao;

import cn.bulletjet.headline.model.News;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface NewsDao extends PagingAndSortingRepository<News, Integer> {

    Page<News> findAll(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update News n set n.commentCount=?2 where n.id=?1")
    public void updateCommentCount(int id, int count);

    @Transactional
    @Modifying
    @Query("update News n set n.likeCount=?2 where n.id=?1")
    public void updateLikeCount(int id, int count);

    public List<News> findAll();
    //Page<News> findAll();

}