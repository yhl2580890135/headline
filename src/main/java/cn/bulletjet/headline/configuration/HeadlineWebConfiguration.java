package cn.bulletjet.headline.configuration;

import cn.bulletjet.headline.interceptor.LoginRequiredInterceptor;
import cn.bulletjet.headline.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.*;


//@EnableWebMvc
//@Configuration
//@ComponentScan
//public class HeadlineWebConfiguration extends WebMvcConfigurationSupport{
//    private ApplicationContext applicationContext;
//    @Autowired
//    PassportInterceptor passportInterceptor;
//
//    @Autowired
//    LoginRequiredInterceptor loginRequiredInterceptor;
//
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        super.addResourceHandlers(registry);
//        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");
//        registry.addResourceHandler("/templates/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/templates/");
//        super.addResourceHandlers(registry);
//    }
//
//    @Override
//    public void setApplicationContext(@Nullable ApplicationContext applicationContext) {
//       super.setApplicationContext(applicationContext);
//        this.applicationContext = applicationContext;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        registry.addInterceptor(passportInterceptor);
//        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*").excludePathPatterns("/");
//        super.addInterceptors(registry);
//    }
//}
@Component
public class HeadlineWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
}