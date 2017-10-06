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

    //保存图片 到本地目录 返回的图片的url
    public String saveImage(MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        int point = file.getOriginalFilename().lastIndexOf(".");//获得最后一个点的位置。
        String suffix = null;
        if (point < 0)
            return null;
        else {
            suffix = file.getOriginalFilename().substring(point + 1).toLowerCase();
            if (!HeadlineUtil.islegal(suffix))
                return null;
            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
            Files.copy(file.getInputStream(), new File(HeadlineUtil.UPLOAD_DIR + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
            //这个返回的是图片的访问地址
            return "image?name=" + fileName;
        }
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
