package cn.bulletjet.headline.service;

import cn.bulletjet.headline.util.HeadlineUtil;
import com.alibaba.fastjson.JSONException;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

/**
 * Created by nowcoder on 2016/7/7.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "jp1_q-8naokYyhfhG6ylelO6x8mo2KxhTW5NRtYY";
    String SECRET_KEY = "JIPosXyRkGEJ1I-deItCgr3ByCjBlPFMmIO6q2AI";
    //要上传的空间
    String bucketname = "headline";
    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    Configuration cfg = new Configuration(Zone.zone0());
    UploadManager uploadManager = new UploadManager(cfg);

    private static String QINIU_IMAGE_DOMAIN = "http://owvymmd66.bkt.clouddn.com/";

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException, JSONException {
        try {
            int point = file.getOriginalFilename().lastIndexOf(".");
            if (point < 0) {
                return null;
            }
            String suffix = file.getOriginalFilename().substring(point + 1).toLowerCase();
            if (!HeadlineUtil.islegal(suffix)) {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());

            //打印返回的信息
            if (res.isOK() && res.isJson()) {
                DefaultPutRet putRet = new Gson().fromJson(res.bodyString(), DefaultPutRet.class);
                return QINIU_IMAGE_DOMAIN + putRet.key;
            } else {
                logger.error("七牛异常:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            logger.error("七牛异常:" + e.getMessage());
            return null;
        }
    }
}
