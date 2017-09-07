package cn.bulletjet.headline.controller;

import cn.bulletjet.headline.service.HeadlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class HelloWorldController {
    private static final Logger logger= LoggerFactory.getLogger(HelloWorldController.class);
    @Autowired
    private HeadlineService headlineService;

    @RequestMapping(path = {"/index.html"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String toIndex(HttpSession session) {
        logger.info("visit first page");
        return "Hello World:" + session.getAttribute("message") + "<br>say:<br>" + headlineService.say() + "<br>";
    }

    @RequestMapping(value={"/profile/{groupId}/{userId}"})
    public @ResponseBody String profile(@PathVariable("groupId") String groupId,
                                            @PathVariable("userId") int userId,
                                            @RequestParam(value = "type",defaultValue = "1") int type,
                                            @RequestParam(value="key",defaultValue = "code") String key)
   {
       return String.format("{%s},{%d},{%d},{%s}",groupId,userId,type,key);
   }

    @RequestMapping(value = {"/vm"})
   public String news(Model model){
       model.addAttribute("value1",100);
       List<String> colors= Arrays.asList(new String[]{"red","yellow","white"});
       Map<String,String> map = new HashMap<String,String>();
       for (int i = 0; i <4 ; i++) {
           map.put(String.valueOf(i),String.valueOf(i*i));
       }
       model.addAttribute("colors",colors);
       model.addAttribute("map",map);
//       model.addAttribute("user",new User("jim"));
       return "news";
   }
   @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest httpRequest,
                          HttpServletResponse httpResponse,
                          HttpSession httpSession){
       StringBuilder sb=new StringBuilder();
        Enumeration<String> e= httpRequest.getHeaderNames();
        while(e.hasMoreElements()){
            String name =e.nextElement();
            sb.append(name+":"+httpRequest.getHeader(name)+"</br>");
        }
        for(Cookie cookie:httpRequest.getCookies()){
            sb.append("cookie:");
            sb.append(cookie.getName()+":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }
       sb.append("getMethod():"+httpRequest.getMethod());
       sb.append("getQueryString():"+httpRequest.getQueryString());
       sb.append("getRequestURI():"+httpRequest.getRequestURI());
       sb.append("getPathInfo():"+httpRequest.getPathInfo());
       return sb.toString();
   }

    @RequestMapping("/response")
    @ResponseBody
    public String response(@CookieValue(value = "nowcoderid",defaultValue = "1") String nowcoderId,
                           @RequestParam(value = "key",defaultValue = "key") String key,
                           @RequestParam(value = "value",defaultValue = "value") String value,
                           HttpServletResponse response){
                           response.addCookie(new Cookie(key,value));
                           response.addHeader(key,value);
                           return "nowcoderId from cookie:"+ nowcoderId;
    }
    @RequestMapping("/redirect/{code}")
    @ResponseBody
    public  String redirectView(@PathVariable("code") int code,HttpSession session
    ){
//        RedirectView rw = new RedirectView("/",true);
//        if(code==301)
//            rw.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
//        return  rw;

        session.setAttribute("message","jump from error ");
        return "redirect:/";//302跳转
    }
    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="key") String key){
        if(key.equals("admin"))
            return "welcome you ,admin";
        else
            throw new IllegalArgumentException("an errorn appers");

    }
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return  "error:"+e.getMessage();

    }

}

