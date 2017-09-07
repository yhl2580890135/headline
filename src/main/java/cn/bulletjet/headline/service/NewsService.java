package cn.bulletjet.headline.service;

import cn.bulletjet.headline.dao.NewsDao;
import cn.bulletjet.headline.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    public List<News> getLatestNews() {
        return newsDao.findAll();
    }

}
