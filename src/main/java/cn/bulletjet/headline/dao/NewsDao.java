package cn.bulletjet.headline.dao;

import cn.bulletjet.headline.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsDao extends JpaRepository<News, Integer> {
}
