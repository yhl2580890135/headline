package cn.bulletjet.headline.service;

import cn.bulletjet.headline.dao.NewsDao;
import cn.bulletjet.headline.model.News;
import cn.bulletjet.headline.util.HeadlineUtil;
import com.sun.jndi.toolkit.dir.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    public List<News> getLatestNews() {
        return newsDao.findAll();
    }

    public News findById(int Id) {
        return newsDao.findById(Id).orElse(null);
    }

    public void addNews(News news) {
        newsDao.save(news);
    }

    public void updateCommentCount(int id, int count) {
        newsDao.updateCommentCount(id, count);
    }

    public void updateLiketCount(int id, int count) {
        newsDao.updateLikeCount(id, count);
    }

    public News getById(int newsId) {
        return newsDao.findById(newsId).orElse(null);
    }
}
